package com.duckduckgo.autoconsent.store

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.duckduckgo.autoconsent.api.AutoconsentFeatureName

interface AutoconsentFeatureToggleStore {
    fun deleteAll()

    fun get(
        featureName: AutoconsentFeatureName,
        defaultValue: Boolean
    ): Boolean

    fun getMinSupportedVersion(featureName: AutoconsentFeatureName): Int

    fun insert(toggle: AutoconsentFeatureToggles)
}

internal class RealAutoconsentFeatureToggleStore(private val context: Context) :
    AutoconsentFeatureToggleStore {
    private val preferences: SharedPreferences
        get() = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE)

    override fun deleteAll() {
        preferences.edit().clear().apply()
    }

    override fun get(featureName: AutoconsentFeatureName, defaultValue: Boolean): Boolean {
        return preferences.getBoolean(featureName.value, defaultValue)
    }

    override fun getMinSupportedVersion(featureName: AutoconsentFeatureName): Int {
        return preferences.getInt("${featureName.value}$MIN_SUPPORTED_VERSION", 0)
    }

    override fun insert(toggle: AutoconsentFeatureToggles) {
        preferences.edit {
            putBoolean(toggle.featureName.value, toggle.enabled)
            toggle.minSupportedVersion?.let {
                putInt("${toggle.featureName.value}$MIN_SUPPORTED_VERSION", it)
            }
        }
    }

    companion object {
        const val FILENAME = "com.duckduckgo.autoconsent.store.toggles"
        const val MIN_SUPPORTED_VERSION = "MinSupportedVersion"
    }
}

data class AutoconsentFeatureToggles(
    val featureName: AutoconsentFeatureName,
    val enabled: Boolean,
    val minSupportedVersion: Int?
)
