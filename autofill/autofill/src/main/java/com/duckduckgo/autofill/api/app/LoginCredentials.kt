package com.duckduckgo.autofill.api.app

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Representation of login credentials used for autofilling into the browser.
 */
@Parcelize
data class LoginCredentials(
    val id: Long? = null,
    val domain: String?,
    val username: String?,
    val password: String?,
    val domainTitle: String? = null,
    val notes: String? = null,
    val lastUpdatedMillis: Long? = null
) : Parcelable {
    override fun toString(): String {
        return "LoginCredentials(id=$id, domain=$domain, username=$username, password=********," +
                " domainTitle=$domainTitle, lastUpdatedMillis=$lastUpdatedMillis, notes=$notes"
    }
}
