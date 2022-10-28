package com.duckduckgo.voice.store

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

interface VoiceSearchDataStore {
    var permissionDeclinedForever: Boolean
    var userAcceptedRationaleDialog: Boolean
    var availabilityLogged: Boolean
}

class SharedPreferencesVoiceSearchDataStore constructor(
    private val context: Context
) : VoiceSearchDataStore {
    companion object {
        const val FILENAME = "com.duckduckgo.app.voice"
        const val KEY_DECLINED_PERMISSION_FOREVER = "KEY_DECLINED_PERMISSION_FOREVER"
        const val KEY_RATIONALE_DIALOG_ACCEPTED = "KEY_RATIONALE_DIALOG_ACCEPTED"
        const val KEY_VOICE_SEARCH_AVAILABILITY_LOGGED = "KEY_VOICE_SEARCH_AVAILABILITY_LOGGED"
    }

    private val preferences: SharedPreferences
        get() = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE)

    override var permissionDeclinedForever: Boolean
        get() = preferences.getBoolean(KEY_DECLINED_PERMISSION_FOREVER, false)
        set(declined) {
            updateValue(KEY_DECLINED_PERMISSION_FOREVER, declined)
        }

    override var userAcceptedRationaleDialog: Boolean
        get() = preferences.getBoolean(KEY_RATIONALE_DIALOG_ACCEPTED, false)
        set(accept) {
            updateValue(KEY_RATIONALE_DIALOG_ACCEPTED, accept)
        }

    override var availabilityLogged: Boolean
        get() = preferences.getBoolean(KEY_VOICE_SEARCH_AVAILABILITY_LOGGED, false)
        set(value) {
            updateValue(KEY_VOICE_SEARCH_AVAILABILITY_LOGGED, value)
        }

    private fun updateValue(
        key: String,
        value: Boolean
    ) {
        preferences.edit(true) {
            putBoolean(key, value)
        }
    }
}
