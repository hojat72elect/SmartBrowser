package com.duckduckgo.privacy.config.api

/** List of [PrivacyFeatureName] that belong to the Privacy Configuration */
enum class PrivacyFeatureName(val value: String) {
    ContentBlockingFeatureName("contentBlocking"),
    GpcFeatureName("gpc"),
    HttpsFeatureName("https"),
    TrackerAllowlistFeatureName("trackerAllowlist"),
    DrmFeatureName("eme"),
    AmpLinksFeatureName("ampLinks"),
    TrackingParametersFeatureName("trackingParameters"),
    AutofillFeatureName("autofill"),
    UserAgentFeatureName("customUserAgent"),
}
