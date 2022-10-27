package com.duckduckgo.adclick.impl

import android.content.Context
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.work.BackoffPolicy
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.duckduckgo.adclick.api.AdClickManager
import com.duckduckgo.anvil.annotations.ContributesWorker
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesMultibinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@ContributesWorker(AppScope::class)
class DataRemovalAdClickWorker(context: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(context, workerParameters) {

    @Inject
    lateinit var adClickManager: AdClickManager

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            adClickManager.clearAllExpiredAsync()

            return@withContext Result.success()
        }
    }
}

@ContributesMultibinding(
    scope = AppScope::class,
    boundType = LifecycleObserver::class
)
class DataRemovalAdClickWorkerScheduler @Inject constructor(
    private val workManager: WorkManager
) : DefaultLifecycleObserver {

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        Timber.v("Scheduling ad click data removal worker")
        val workerRequest = PeriodicWorkRequestBuilder<DataRemovalAdClickWorker>(12, TimeUnit.HOURS)
            .addTag(DATA_REMOVAL_AD_CLICK_WORKER_TAG)
            .setBackoffCriteria(BackoffPolicy.LINEAR, 10, TimeUnit.MINUTES)
            .build()
        workManager.enqueueUniquePeriodicWork(
            DATA_REMOVAL_AD_CLICK_WORKER_TAG,
            ExistingPeriodicWorkPolicy.REPLACE,
            workerRequest
        )
    }

    companion object {
        private const val DATA_REMOVAL_AD_CLICK_WORKER_TAG = "DATA_REMOVAL_AD_CLICK_WORKER_TAG"
    }
}
