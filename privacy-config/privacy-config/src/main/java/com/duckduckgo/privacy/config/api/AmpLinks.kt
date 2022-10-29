package com.duckduckgo.privacy.config.api

/** Public interface for the AMP Links feature */
interface AmpLinks {
    /**
     * This method takes a [url] and returns `true` or `false` depending if the [url] is in the
     * AMP links exceptions list
     * @return `true` if the given [url] is in the AMP links exceptions list and `false`
     * otherwise.
     */
    fun isAnException(url: String): Boolean

    /**
     * This method takes a [url] and returns a [AmpLinkType] depending on the detected AMP
     * link.
     * @return the [AmpLinkType] or `null` if the [url] is not an AMP link.
     */
    fun extractCanonicalFromAmpLink(url: String): AmpLinkType?

    /** The last AMP link and its destination URL. */
    var lastAmpLinkInfo: AmpLinkInfo?
}

/** Public data class for AMP Link Info. */
data class AmpLinkInfo(val ampLink: String, var destinationUrl: String? = null)

/** Public data class for AMP Link Exceptions. */
data class AmpLinkException(val domain: String, val reason: String)

/** Public sealed class for AMP Link Type. */
sealed class AmpLinkType {
    class ExtractedAmpLink(val extractedUrl: String) : AmpLinkType()
    class CloakedAmpLink(val ampUrl: String) : AmpLinkType()
}
