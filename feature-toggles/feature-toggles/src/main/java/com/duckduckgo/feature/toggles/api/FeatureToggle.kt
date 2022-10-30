package com.duckduckgo.feature.toggles.api

/** Any feature toggles implemented in any module should implement [FeatureToggle] */
interface FeatureToggle {
    /**
     * This method takes a [featureName] and optionally a default value.
     * @return `true` if the feature is enabled, `false` if is not
     * @throws [IllegalArgumentException] if the feature is not implemented
     */
    fun isFeatureEnabled(
        featureName: String,
        defaultValue: Boolean = true
    ): Boolean
}
