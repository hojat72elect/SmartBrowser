package com.duckduckgo.voice.api

interface VoiceSearchAvailability {
    val isVoiceSearchSupported: Boolean
    fun shouldShowVoiceSearch(
        isEditing: Boolean = false,
        urlLoaded: String = ""
    ): Boolean
}
