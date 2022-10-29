package com.duckduckgo.autoconsent.store

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

interface AutoconsentSettingsDataStore {
    var userSetting: Boolean
    var firstPopupHandled: Boolean
}

class RealAutoconsentSettingsDataStore constructor(private val context: Context) :
    AutoconsentSettingsDataStore {

    private val preferences: SharedPreferences
        get() = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE)

    override var userSetting: Boolean
        get() = preferences.getBoolean(AUTOCONSENT_USER_SETTING, false)
        set(value) {
            preferences.edit(commit = true) {
                putBoolean(AUTOCONSENT_USER_SETTING, value)
            }
        }

    override var firstPopupHandled: Boolean
        get() = preferences.getBoolean(AUTOCONSENT_FIRST_POPUP_HANDLED, false)
        set(value) {
            preferences.edit(commit = true) {
                putBoolean(AUTOCONSENT_FIRST_POPUP_HANDLED, value)
            }
        }

    companion object {
        private const val FILENAME = "com.duckduckgo.autoconsent.store.settings"
        private const val AUTOCONSENT_USER_SETTING = "AutoconsentUserSetting"
        private const val AUTOCONSENT_FIRST_POPUP_HANDLED = "AutoconsentFirstPopupHandled"
    }
}
