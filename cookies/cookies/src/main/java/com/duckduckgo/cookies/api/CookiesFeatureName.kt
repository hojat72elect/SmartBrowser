package com.duckduckgo.cookies.api

/** List of [CookiesFeatureName] that belong to the Cookies feature */
enum class CookiesFeatureName(val value: String) {
    Cookie("cookie"),
}

/** Public data class for Cookie Exceptions. */
data class CookieException(val domain: String, val reason: String)
