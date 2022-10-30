package com.duckduckgo.autofill.api

import android.os.Parcelable
import androidx.fragment.app.DialogFragment
import com.duckduckgo.autofill.api.app.LoginCredentials
import com.duckduckgo.autofill.api.app.LoginTriggerType
import kotlinx.parcelize.Parcelize

/**
 * Dialog which can be shown when user is required to select which saved credential to autofill
 */
interface CredentialAutofillPickerDialog {

    companion object {

        fun resultKey(tabId: String) = "$tabId/CredentialAutofillPickerDialogResult"

        const val TAG = "CredentialAutofillPickerDialog"
        const val KEY_CANCELLED = "cancelled"
        const val KEY_URL = "url"
        const val KEY_CREDENTIALS = "credentials"
        const val KEY_TRIGGER_TYPE = "triggerType"
        const val KEY_TAB_ID = "tabId"
    }
}

/**
 * Dialog which can be shown to prompt user to save credentials or not
 */
interface CredentialSavePickerDialog {

    companion object {
        fun resultKeyUserChoseToSaveCredentials(tabId: String) =
            "$tabId/CredentialSavePickerDialogResultSave"

        fun resultKeyShouldPromptToDisableAutofill(tabId: String) =
            "$tabId/CredentialSavePickerDialogResultShouldPromptToDisableAutofill"

        const val TAG = "CredentialSavePickerDialog"
        const val KEY_URL = "url"
        const val KEY_CREDENTIALS = "credentials"
        const val KEY_TAB_ID = "tabId"
    }
}

/**
 * Dialog which can be shown to prompt user to update existing saved credentials or not
 */
interface CredentialUpdateExistingCredentialsDialog {

    @Parcelize
    sealed interface CredentialUpdateType : Parcelable {

        @Parcelize
        object Username : CredentialUpdateType

        @Parcelize
        object Password : CredentialUpdateType
    }

    companion object {
        fun resultKey(tabId: String) = "$tabId/CredentialUpdateExistingCredentialsResult"

        const val TAG = "CredentialUpdateExistingCredentialsDialog"
        const val KEY_URL = "url"
        const val KEY_CREDENTIALS = "credentials"
        const val KEY_TAB_ID = "tabId"
        const val KEY_CREDENTIAL_UPDATE_TYPE = "updateType"
    }
}

/**
 * Factory used to get instances of the various autofill dialogs
 */
interface CredentialAutofillDialogFactory {

    fun autofillSelectCredentialsDialog(
        url: String,
        credentials: List<LoginCredentials>,
        triggerType: LoginTriggerType,
        tabId: String
    ): DialogFragment

    fun autofillSavingCredentialsDialog(
        url: String,
        credentials: LoginCredentials,
        tabId: String
    ): DialogFragment

    fun autofillSavingUpdatePasswordDialog(
        url: String,
        credentials: LoginCredentials,
        tabId: String
    ): DialogFragment

    fun autofillSavingUpdateUsernameDialog(
        url: String,
        credentials: LoginCredentials,
        tabId: String
    ): DialogFragment

}
