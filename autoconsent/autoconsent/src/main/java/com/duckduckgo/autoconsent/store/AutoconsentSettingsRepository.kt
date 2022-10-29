package com.duckduckgo.autoconsent.store

import android.content.Context

interface AutoconsentSettingsRepository : AutoconsentSettingsDataStore {
    companion object {
        fun create(
            context: Context,
        ): AutoconsentSettingsRepository {
            val store = RealAutoconsentSettingsDataStore(context)
            return RealAutoconsentSettingsRepository(store)
        }
    }
}

internal class RealAutoconsentSettingsRepository constructor(
    private val autoconsentSettingsDataStore: AutoconsentSettingsDataStore,
) : AutoconsentSettingsRepository, AutoconsentSettingsDataStore by autoconsentSettingsDataStore
