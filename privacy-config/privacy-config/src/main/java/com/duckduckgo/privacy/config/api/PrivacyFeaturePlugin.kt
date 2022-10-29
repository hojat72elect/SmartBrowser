package com.duckduckgo.privacy.config.api

/**
 * Implement this interface and contribute it as a multibinding to get called upon downloading remote privacy config
 *
 * Usage:
 *
 * ```kotlin
 * @ContributesMultibinding(AppScope::class)
 * class MuFeaturePlugin @Inject constructor(...) : PrivacyFeaturePlugin {
 *
 * }
 * ```
 */
interface PrivacyFeaturePlugin {
    /**
     * @return `true` when the feature was stored, otherwise `false`
     */
    fun store(
        featureName: String,
        jsonString: String
    ): Boolean

    /**
     * @return the [featureName] of this feature
     */
    val featureName: String
}
