package com.duckduckgo.privacy.config.api

/** Public interface for the DRM feature */
interface Drm {
    /**
     * This method takes a [url] and an [Array<String>] returns an `Array<String>` depending on the
     * [url]
     * @return an `Array<String>` if the given [url] is in the eme list and an empty array
     * otherwise.
     */
    fun getDrmPermissionsForRequest(
        url: String,
        resources: Array<String>
    ): Array<String>
}

/** Public data class for Drm Exceptions */
data class DrmException(
    val domain: String,
    val reason: String
)
