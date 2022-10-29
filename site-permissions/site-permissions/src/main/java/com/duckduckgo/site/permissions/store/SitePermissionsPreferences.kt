package com.duckduckgo.site.permissions.store

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

interface SitePermissionsPreferences {

    var askCameraEnabled: Boolean
    var askMicEnabled: Boolean
}

class SitePermissionsPreferencesImp @Inject constructor(private val context: Context) :
    SitePermissionsPreferences {

    private val preferences: SharedPreferences
        get() = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE)

    override var askCameraEnabled: Boolean
        get() = preferences.getBoolean(KEY_ASK_CAMERA_ENABLED, true)
        set(enabled) = preferences.edit { putBoolean(KEY_ASK_CAMERA_ENABLED, enabled) }

    override var askMicEnabled: Boolean
        get() = preferences.getBoolean(KEY_ASK_MIC_ENABLED, true)
        set(enabled) = preferences.edit { putBoolean(KEY_ASK_MIC_ENABLED, enabled) }

    companion object {
        const val FILENAME = "com.duckduckgo.site.permissions.settings"
        const val KEY_ASK_CAMERA_ENABLED = "ASK_CAMERA_ENABLED"
        const val KEY_ASK_MIC_ENABLED = "ASK_MIC_ENABLED"
    }
}

@SuppressLint("ApplySharedPref")
inline fun SharedPreferences.edit(
    commit: Boolean = false,
    action: SharedPreferences.Editor.() -> Unit
) {
    val editor = edit()
    action(editor)
    if (commit) {
        editor.commit()
    } else {
        editor.apply()
    }
}
