package com.duckduckgo.privacy.config.api

/** Public interface for the Autofill feature */
interface Autofill {
    /**
     * This method takes a [url] and returns `true` or `false` depending if the [url] is in the
     * autofill exceptions list
     * @return a `true` if the given [url] if the url is in the autofill exceptions list and `false`
     * otherwise.
     */
    fun isAnException(url: String): Boolean
}

/** Public data class for Autofill Exceptions */
data class AutofillException(
    val domain: String,
    val reason: String
)
