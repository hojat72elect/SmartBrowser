package com.duckduckgo.voice.store

interface VoiceSearchRepository {
    fun declinePermissionForever()
    fun acceptRationaleDialog()
    fun saveLoggedAvailability()
    fun getHasPermissionDeclinedForever(): Boolean
    fun getHasAcceptedRationaleDialog(): Boolean
    fun getHasLoggedAvailability(): Boolean
}

class RealVoiceSearchRepository constructor(
    private val dataStore: VoiceSearchDataStore
) : VoiceSearchRepository {
    override fun declinePermissionForever() {
        dataStore.permissionDeclinedForever = true
    }

    override fun acceptRationaleDialog() {
        dataStore.userAcceptedRationaleDialog = true
    }

    override fun saveLoggedAvailability() {
        dataStore.availabilityLogged = true
    }

    override fun getHasPermissionDeclinedForever(): Boolean = dataStore.permissionDeclinedForever

    override fun getHasAcceptedRationaleDialog(): Boolean = dataStore.userAcceptedRationaleDialog

    override fun getHasLoggedAvailability(): Boolean = dataStore.availabilityLogged
}
