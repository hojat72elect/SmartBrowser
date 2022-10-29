package com.duckduckgo.privacy.config.store.features.unprotectedtemporary

import com.duckduckgo.app.global.DispatcherProvider
import com.duckduckgo.privacy.config.store.PrivacyConfigDatabase
import com.duckduckgo.privacy.config.store.UnprotectedTemporaryEntity
import com.duckduckgo.privacy.config.store.toUnprotectedTemporaryException
import java.util.concurrent.CopyOnWriteArrayList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

interface UnprotectedTemporaryRepository {
    fun updateAll(exceptions: List<UnprotectedTemporaryEntity>)
    val exceptions: CopyOnWriteArrayList<com.duckduckgo.privacy.config.api.UnprotectedTemporaryException>
}

class RealUnprotectedTemporaryRepository(
    val database: PrivacyConfigDatabase,
    coroutineScope: CoroutineScope,
    dispatcherProvider: DispatcherProvider
) : UnprotectedTemporaryRepository {

    private val unprotectedTemporaryDao: UnprotectedTemporaryDao =
        database.unprotectedTemporaryDao()
    override val exceptions =
        CopyOnWriteArrayList<com.duckduckgo.privacy.config.api.UnprotectedTemporaryException>()

    init {
        coroutineScope.launch(dispatcherProvider.io()) { loadToMemory() }
    }

    override fun updateAll(exceptions: List<UnprotectedTemporaryEntity>) {
        unprotectedTemporaryDao.updateAll(exceptions)
        loadToMemory()
    }

    private fun loadToMemory() {
        exceptions.clear()
        unprotectedTemporaryDao.getAll()
            .map { exceptions.add(it.toUnprotectedTemporaryException()) }
    }
}
