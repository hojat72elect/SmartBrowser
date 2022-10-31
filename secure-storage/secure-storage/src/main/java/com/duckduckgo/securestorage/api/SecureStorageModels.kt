package com.duckduckgo.securestorage.api

/**
 * Public data class that wraps all data related to a website login. All succeeding l2 and above attributes should be added here directly.
 *
 * [details] contains all l1 encrypted attributes
 * [password] plain text password.
 * [notes] plain text notes associated to a login credential
 */
data class WebsiteLoginDetailsWithCredentials(
    val details: WebsiteLoginDetails,
    val password: String?,
    val notes: String? = null,
)

/**
 * Public data class that wraps all data that should only be covered with l1 encryption.
 * All attributes is in plain text. Also, all should not require user authentication to be decrypted.
 *
 * [domain] url/name associated to a website login.
 * [username] used to populate the username fields in a login
 * [id] database id associated to the website login
 * [domainTitle] title associated to the login
 * [lastUpdatedMillis] time in milliseconds when the credential was last updated
 */
data class WebsiteLoginDetails(
    val domain: String?,
    val username: String?,
    val id: Long? = null,
    val domainTitle: String? = null,
    val lastUpdatedMillis: Long? = null
)
