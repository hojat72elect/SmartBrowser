package com.duckduckgo.cookies.store

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

interface CookiesFeatureToggleStore {
    fun deleteAll()

    fun get(
        featureName: com.duckduckgo.cookies.api.CookiesFeatureName,
        defaultValue: Boolean
    ): Boolean

    fun getMinSupportedVersion(featureName: com.duckduckgo.cookies.api.CookiesFeatureName): Int

    fun insert(toggle: CookiesFeatureToggles)
}

class RealCookiesFeatureToggleStore(private val context: Context) : CookiesFeatureToggleStore {
    private val preferences: SharedPreferences
        get() = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE)

    override fun deleteAll() {
        preferences.edit().clear().apply()
    }

    override fun get(
        featureName: com.duckduckgo.cookies.api.CookiesFeatureName,
        defaultValue: Boolean
    ): Boolean {
        return preferences.getBoolean(featureName.value, defaultValue)
    }

    override fun getMinSupportedVersion(featureName: com.duckduckgo.cookies.api.CookiesFeatureName): Int {
        return preferences.getInt("${featureName.value}$MIN_SUPPORTED_VERSION", 0)
    }

    override fun insert(toggle: CookiesFeatureToggles) {
        preferences.edit {
            putBoolean(toggle.featureName.value, toggle.enabled)
            toggle.minSupportedVersion?.let {
                putInt("${toggle.featureName.value}$MIN_SUPPORTED_VERSION", it)
            }
        }
    }

    companion object {
        const val FILENAME = "com.duckduckgo.cookies.store.toggles"
        const val MIN_SUPPORTED_VERSION = "MinSupportedVersion"
    }
}

data class CookiesFeatureToggles(
    val featureName: com.duckduckgo.cookies.api.CookiesFeatureName,
    val enabled: Boolean,
    val minSupportedVersion: Int?
)
