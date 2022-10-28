package com.duckduckgo.app.traces

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

/**
 * Implements the tracing API
 */
@ContributesBinding(AppScope::class)
class RealStartupTraces @Inject constructor(
    private val context: Context
) : StartupTraces {

    private val preferences: SharedPreferences
        get() = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE)

    override var isTraceEnabled: Boolean
        get() = preferences.getBoolean(ENABLE_KEY, false)
        set(value) {
            preferences.edit(true) { putBoolean(ENABLE_KEY, value) }
        }

    companion object {
        private const val FILENAME = "com.duckduckgo.app.traces.preference"
        private const val ENABLE_KEY = "com.duckduckgo.app.traces.preference.enable"
    }
}
