package com.duckduckgo.site.permissions.api

/** Public interface for managing site permissions data */
interface SitePermissionsManager {

    /**
     * Returns an array of already granted site permissions. That could be:
     *     - Permission is always allowed for this website
     *     - Permission has been granted within 24h for same site and same tab
     *
     * @param url unmodified URL taken from the URL bar (containing subdomains, query params etc...)
     * @param tabId id from the tab where this method is called from
     * @param resources array of permissions that have been requested by the website
     */
    suspend fun getSitePermissionsGranted(
        url: String,
        tabId: String,
        resources: Array<String>
    ): Array<String>

    /**
     * Returns an array of permissions that we support and user didn't deny for given website
     *
     * @param url unmodified URL taken from the URL bar (containing subdomains, query params etc...)
     * @param resources array of permissions that have been requested by the website
     */
    suspend fun getSitePermissionsAllowedToAsk(url: String, resources: Array<String>): Array<String>

    /**
     * Deletes all site permissions but the ones that are fireproof
     *
     * @param fireproofDomains list of domains that are fireproof
     */
    suspend fun clearAllButFireproof(fireproofDomains: List<String>)
}
