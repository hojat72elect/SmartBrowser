package com.duckduckgo.cookies.store

import com.duckduckgo.app.global.DispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.concurrent.CopyOnWriteArrayList

interface CookiesRepository {
    fun updateAll(
        exceptions: List<CookieExceptionEntity>,
        firstPartyTrackerCookiePolicy: FirstPartyCookiePolicyEntity
    )

    var firstPartyCookiePolicy: FirstPartyCookiePolicyEntity
    val exceptions: List<com.duckduckgo.cookies.api.CookieException>
}

class RealCookieRepository constructor(
    val database: CookiesDatabase,
    coroutineScope: CoroutineScope,
    dispatcherProvider: DispatcherProvider
) : CookiesRepository {

    private val cookiesDao: CookiesDao = database.cookiesDao()

    override val exceptions = CopyOnWriteArrayList<com.duckduckgo.cookies.api.CookieException>()
    override var firstPartyCookiePolicy =
        FirstPartyCookiePolicyEntity(threshold = DEFAULT_THRESHOLD, maxAge = DEFAULT_MAX_AGE)

    init {
        coroutineScope.launch(dispatcherProvider.io()) {
            loadToMemory()
        }
    }

    override fun updateAll(
        exceptions: List<CookieExceptionEntity>,
        firstPartyTrackerCookiePolicy: FirstPartyCookiePolicyEntity
    ) {
        cookiesDao.updateAll(exceptions, firstPartyTrackerCookiePolicy)
        loadToMemory()
    }

    private fun loadToMemory() {
        exceptions.clear()
        cookiesDao.getAllCookieExceptions().map {
            exceptions.add(it.toCookieException())
        }
        firstPartyCookiePolicy =
            cookiesDao.getFirstPartyCookiePolicy()
                ?: FirstPartyCookiePolicyEntity(
                    threshold = DEFAULT_THRESHOLD,
                    maxAge = DEFAULT_MAX_AGE
                )
    }

    companion object {
        const val DEFAULT_THRESHOLD = 86400
        const val DEFAULT_MAX_AGE = 86400
    }
}
