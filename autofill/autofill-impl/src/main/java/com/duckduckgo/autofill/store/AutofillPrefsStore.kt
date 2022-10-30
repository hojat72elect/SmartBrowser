package com.duckduckgo.autofill.store

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

interface AutofillPrefsStore {
    var isEnabled: Boolean
    var showOnboardingWhenOfferingToSaveLogin: Boolean
    var autofillDeclineCount: Int
    var monitorDeclineCounts: Boolean
}

class RealAutofillPrefsStore constructor(
    private val applicationContext: Context
) : AutofillPrefsStore {

    private val prefs: SharedPreferences by lazy {
        applicationContext.getSharedPreferences(FILENAME, Context.MODE_PRIVATE)
    }

    override var isEnabled: Boolean
        get() = prefs.getBoolean(AUTOFILL_ENABLED, true)
        set(value) = prefs.edit {
            putBoolean(AUTOFILL_ENABLED, value)
        }

    override var showOnboardingWhenOfferingToSaveLogin: Boolean
        get() = prefs.getBoolean(SHOW_SAVE_LOGIN_ONBOARDING, true)
        set(value) = prefs.edit { putBoolean(SHOW_SAVE_LOGIN_ONBOARDING, value) }

    override var autofillDeclineCount: Int
        get() = prefs.getInt(AUTOFILL_DECLINE_COUNT, 0)
        set(value) = prefs.edit { putInt(AUTOFILL_DECLINE_COUNT, value) }

    override var monitorDeclineCounts: Boolean
        get() = prefs.getBoolean(MONITOR_AUTOFILL_DECLINES, true)
        set(value) = prefs.edit { putBoolean(MONITOR_AUTOFILL_DECLINES, value) }

    companion object {
        const val FILENAME = "com.duckduckgo.autofill.store.autofill_store"
        const val AUTOFILL_ENABLED = "autofill_enabled"
        const val SHOW_SAVE_LOGIN_ONBOARDING = "autofill_show_onboardind_saved_login"
        const val AUTOFILL_DECLINE_COUNT = "autofill_decline_count"
        const val MONITOR_AUTOFILL_DECLINES = "monitor_autofill_declines"
    }
}
