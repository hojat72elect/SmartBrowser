package com.duckduckgo.privacy.config.store.features.drm

import com.duckduckgo.app.global.DispatcherProvider
import com.duckduckgo.privacy.config.store.DrmExceptionEntity
import com.duckduckgo.privacy.config.store.PrivacyConfigDatabase
import com.duckduckgo.privacy.config.store.toDrmException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.concurrent.CopyOnWriteArrayList

interface DrmRepository {
    fun updateAll(exceptions: List<DrmExceptionEntity>)
    val exceptions: CopyOnWriteArrayList<com.duckduckgo.privacy.config.api.DrmException>
}

class RealDrmRepository(
    val database: PrivacyConfigDatabase,
    coroutineScope: CoroutineScope,
    dispatcherProvider: DispatcherProvider
) :
    DrmRepository {

    private val drmDao: DrmDao = database.drmDao()
    override val exceptions = CopyOnWriteArrayList<com.duckduckgo.privacy.config.api.DrmException>()

    init {
        coroutineScope.launch(dispatcherProvider.io()) {
            loadToMemory()
        }
    }

    override fun updateAll(exceptions: List<DrmExceptionEntity>) {
        drmDao.updateAll(exceptions)
        loadToMemory()
    }

    private fun loadToMemory() {
        exceptions.clear()
        drmDao.getAll().map {
            exceptions.add(it.toDrmException())
        }
    }
}
