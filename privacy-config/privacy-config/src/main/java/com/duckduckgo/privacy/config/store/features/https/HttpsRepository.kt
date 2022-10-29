package com.duckduckgo.privacy.config.store.features.https

import com.duckduckgo.app.global.DispatcherProvider
import com.duckduckgo.privacy.config.store.HttpsExceptionEntity
import com.duckduckgo.privacy.config.store.PrivacyConfigDatabase
import com.duckduckgo.privacy.config.store.toHttpsException
import java.util.concurrent.CopyOnWriteArrayList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

interface HttpsRepository {
    fun updateAll(exceptions: List<HttpsExceptionEntity>)
    val exceptions: CopyOnWriteArrayList<com.duckduckgo.privacy.config.api.HttpsException>
}

class RealHttpsRepository(
    val database: PrivacyConfigDatabase,
    coroutineScope: CoroutineScope,
    dispatcherProvider: DispatcherProvider
) : HttpsRepository {

    private val httpsDao: HttpsDao = database.httpsDao()
    override val exceptions =
        CopyOnWriteArrayList<com.duckduckgo.privacy.config.api.HttpsException>()

    init {
        coroutineScope.launch(dispatcherProvider.io()) { loadToMemory() }
    }

    override fun updateAll(exceptions: List<HttpsExceptionEntity>) {
        httpsDao.updateAll(exceptions)
        loadToMemory()
    }

    private fun loadToMemory() {
        exceptions.clear()
        httpsDao.getAll().map { exceptions.add(it.toHttpsException()) }
    }
}
