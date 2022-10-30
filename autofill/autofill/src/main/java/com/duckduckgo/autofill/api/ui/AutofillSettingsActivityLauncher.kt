package com.duckduckgo.autofill.api.ui

import android.content.Context
import android.content.Intent
import com.duckduckgo.autofill.api.app.LoginCredentials

/**
 * Used to access an Intent which will launch the autofill settings activity
 * The activity is implemented in the impl module and is otherwise inaccessible from outside this module.
 */
interface AutofillSettingsActivityLauncher {

    /**
     * Launch the Autofill management activity.
     * Optionally, can provide LoginCredentials to jump directly into viewing mode.
     * If no LoginCredentials provided, will show the list mode.
     */
    fun intent(context: Context, loginCredentials: LoginCredentials? = null): Intent
}
