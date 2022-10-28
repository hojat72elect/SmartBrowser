package com.duckduckgo.app.traces

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.os.Debug
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesMultibinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

/**
 * Implements the tracing API
 */
@ContributesMultibinding(
    scope = AppScope::class,
    boundType = LifecycleObserver::class
)
class AppStartUpTracer @Inject constructor() : ContentProvider(), LifecycleEventObserver {

    // content provide shall have empty constructor
    private val startupTraces: StartupTraces by lazy { RealStartupTraces(context!!.applicationContext) }

    override fun onStateChanged(
        source: LifecycleOwner,
        event: Lifecycle.Event
    ) {
        if (event == Lifecycle.Event.ON_START) {
            Debug.stopMethodTracing()
        }
    }

    override fun onCreate(): Boolean {
        if (startupTraces.isTraceEnabled) {
            val tracesDirPath = context!!.applicationInfo.dataDir
            val fileNameFormat = SimpleDateFormat(
                "yyyy-MM-dd_HH-mm-ss_SSS'.trace'",
                Locale.US
            )
            val fileName = fileNameFormat.format(Date())
            val traceFilePath = "$tracesDirPath/$fileName"
            // Save up to 50Mb data.
            val maxBufferSize = 50 * 1000 * 1000
            // Sample every 1000 microsecond (1ms)
            val samplingIntervalUs = 1000
            Debug.startMethodTracingSampling(
                traceFilePath,
                maxBufferSize,
                samplingIntervalUs
            )
        }

        return false
    }

    override fun update(
        p0: Uri,
        p1: ContentValues?,
        p2: String?,
        p3: Array<out String>?
    ): Int {
        TODO("Not yet implemented")
    }

    override fun insert(
        p0: Uri,
        p1: ContentValues?
    ): Uri? {
        TODO("Not yet implemented")
    }

    override fun delete(
        p0: Uri,
        p1: String?,
        p2: Array<out String>?
    ): Int {
        TODO("Not yet implemented")
    }

    override fun getType(p0: Uri): String? {
        TODO("Not yet implemented")
    }

    override fun query(
        p0: Uri,
        p1: Array<out String>?,
        p2: String?,
        p3: Array<out String>?,
        p4: String?
    ): Cursor? {
        TODO("Not yet implemented")
    }
}
