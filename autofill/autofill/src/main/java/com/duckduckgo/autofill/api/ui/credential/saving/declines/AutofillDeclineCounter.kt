package com.duckduckgo.autofill.api.ui.credential.saving.declines

/**
 * Repeated prompts to use Autofill (e.g., save login credentials) might annoy a user who doesn't want to use Autofill.
 * If the user has declined too many times without using it, we will prompt them to disable.
 *
 * This class is used to track the number of times a user has declined to use Autofill when prompted.
 * It should be permanently disabled, by calling disableDeclineCounter(), when user:
 *    - saves a credential, or
 *    - chooses to disable autofill when prompted to disable autofill, or
 *    - chooses to keep using autofill when prompted to disable autofill
 */
interface AutofillDeclineCounter {

    /**
     * Should be called every time a user declines to save credentials
     * @param domain the domain of the site the user declined to save credentials for
     */
    suspend fun userDeclinedToSaveCredentials(domain: String?)

    /**
     * Determine if the user should be prompted to disable autofill
     * @return true if the user should be prompted to disable autofill
     */
    suspend fun shouldPromptToDisableAutofill(): Boolean

    /**
     * Permanently disable the autofill decline counter
     */
    suspend fun disableDeclineCounter()

}
