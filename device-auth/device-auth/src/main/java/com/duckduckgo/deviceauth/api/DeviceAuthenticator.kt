package com.duckduckgo.deviceauth.api

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

interface DeviceAuthenticator {
    /**
     * This method can be used to check if the user's device has a valid device authentication enrolled (Fingerprint, PIN, pattern or password).
     */
    fun hasValidDeviceAuthentication(): Boolean

    /**
     * Launches a device authentication flow for a specific [featureToAuth] from a [fragment]. [onResult] can be used to
     * communicate back to the feature the result of the flow.
     */
    fun authenticate(
        featureToAuth: Features,
        fragment: Fragment,
        onResult: (AuthResult) -> Unit
    )

    /**
     * Launches a device authentication flow for a specific [featureToAuth] from a [fragmentActivity]. [onResult] can be used to
     * communicate back to the feature the result of the flow.
     */
    fun authenticate(
        featureToAuth: Features,
        fragmentActivity: FragmentActivity,
        onResult: (AuthResult) -> Unit
    )

    sealed class AuthResult {
        object Success : AuthResult()
        object UserCancelled : AuthResult()
        data class Error(val reason: String) : AuthResult()
    }

    enum class Features {
        AUTOFILL
    }
}
