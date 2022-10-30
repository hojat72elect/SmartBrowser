package com.duckduckgo.autofill.api.ui

import com.duckduckgo.autofill.api.store.AutofillStore

/**
 * Used to determine if the given credential details exist in the autofill storage
 *
 * There are times when the UI from the main app will need to prompt the user if they want to update saved details.
 * We can only show that prompt if we've first determined there is an existing partial match in need of an update.
 */
interface ExistingCredentialMatchDetector {
    suspend fun determine(
        currentUrl: String,
        username: String?,
        password: String?
    ): AutofillStore.ContainsCredentialsResult
}
