package com.duckduckgo.autofill.api

import android.webkit.WebView
import com.duckduckgo.autofill.api.app.LoginCredentials
import com.duckduckgo.autofill.api.app.LoginTriggerType

/**
 * Public interface for accessing and configuring browser autofill functionality for a WebView instance
 */
interface BrowserAutofill {
    interface Configurator {
        /**
         * Configures autofill for the current webpage.
         * This should be called once per page load (e.g., onPageStarted())
         *
         * Responsible for injecting the required autofill configuration to the JS layer
         */
        fun configureAutofillForCurrentPage(webView: WebView, url: String?)
    }

    /**
     * Adds the native->JS interface to the given WebView
     * This should be called once per WebView where autofill is to be available in it
     */
    fun addJsInterface(webView: WebView, callback: Callback)

    /**
     * Removes the JS interface as a clean-up. Recommended to call from onDestroy() of Fragment/Activity containing the WebView
     */
    fun removeJsInterface()

    /**
     * Communicates with the JS layer to pass the given credentials
     *
     * @param credentials The credentials to be passed to the JS layer. Can be null to indicate credentials won't be autofilled.
     */
    fun injectCredentials(credentials: LoginCredentials?)

}

/**
 * Browser Autofill callbacks
 */
interface Callback {
    suspend fun onCredentialsAvailableToInject(
        credentials: List<LoginCredentials>,
        triggerType: LoginTriggerType
    )

    suspend fun onCredentialsAvailableToSave(currentUrl: String, credentials: LoginCredentials)
    fun noCredentialsAvailable(originalUrl: String)
}
