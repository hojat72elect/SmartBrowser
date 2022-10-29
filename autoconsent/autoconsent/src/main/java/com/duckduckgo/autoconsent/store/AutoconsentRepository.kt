package com.duckduckgo.autoconsent.store

import com.duckduckgo.app.global.DispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.concurrent.CopyOnWriteArrayList

interface AutoconsentRepository {
    fun updateAll(
        exceptions: List<AutoconsentExceptionEntity>,
        disabledCmps: List<DisabledCmpsEntity>
    )

    val exceptions: List<AutoconsentExceptionEntity>
    val disabledCmps: List<DisabledCmpsEntity>
}

class RealAutoconsentRepository constructor(
    val database: AutoconsentDatabase,
    coroutineScope: CoroutineScope,
    dispatcherProvider: DispatcherProvider
) : AutoconsentRepository {

    private val autoconsentDao: AutoconsentDao = database.autoconsentDao()

    override val exceptions = CopyOnWriteArrayList<AutoconsentExceptionEntity>()
    override val disabledCmps = CopyOnWriteArrayList<DisabledCmpsEntity>()

    init {
        coroutineScope.launch(dispatcherProvider.io()) {
            loadToMemory()
        }
    }

    override fun updateAll(
        exceptions: List<AutoconsentExceptionEntity>,
        disabledCmps: List<DisabledCmpsEntity>
    ) {
        autoconsentDao.updateAll(exceptions, disabledCmps)
        loadToMemory()
    }

    private fun loadToMemory() {
        exceptions.clear()
        exceptions.addAll(autoconsentDao.getExceptions())

        disabledCmps.clear()
        disabledCmps.addAll(autoconsentDao.getDisabledCmps())
    }
}
