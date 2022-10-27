package com.duckduckgo.adclick.impl

import com.duckduckgo.adclick.api.AdClickFeatureName

/**
 * Convenience method to get the [AdCLickFeatureName] from its [String] value
 */
fun adClickFeatureValueOf(value: String): AdClickFeatureName? {
    return AdClickFeatureName.values().find { it.value == value }
}
