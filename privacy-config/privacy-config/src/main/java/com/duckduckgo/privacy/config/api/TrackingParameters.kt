package com.duckduckgo.privacy.config.api

/** Public interface for the Tracking Parameters feature */
interface TrackingParameters {
    /**
     * This method takes an optional [initiatingUrl] and a [url]. It returns `true` or `false` depending if the [url] or [initiatingUrl]
     * is in the tracking parameters exceptions list.
     * @return `true` if the given [url] or [initiatingUrl] is in the tracking parameters exceptions list and `false` otherwise.
     */
    fun isAnException(initiatingUrl: String?, url: String): Boolean

    /**
     * This method takes an optional [initiatingUrl] and a [url] and returns a [String] containing the cleaned URL with tracking parameters removed.
     * @return the URL [String] or `null` if the [url] does not contain tracking parameters.
     */
    fun cleanTrackingParameters(initiatingUrl: String?, url: String): String?

    /** The last tracking parameter cleaned URL. */
    var lastCleanedUrl: String?
}

/** Public data class for Tracking Parameter Exceptions. */
data class TrackingParameterException(val domain: String, val reason: String)
