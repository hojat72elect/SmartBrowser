package com.duckduckgo.autoconsent.store

import android.content.Context

interface AutoconsentFeatureToggleRepository : AutoconsentFeatureToggleStore {
    companion object {
        fun create(
            context: Context,
        ): AutoconsentFeatureToggleRepository {
            val store = RealAutoconsentFeatureToggleStore(context)
            return RealAutoconsentFeatureToggleRepository(store)
        }
    }
}

internal class RealAutoconsentFeatureToggleRepository constructor(
    private val autoconsentFeatureToggleStore: AutoconsentFeatureToggleStore,
) : AutoconsentFeatureToggleRepository,
    AutoconsentFeatureToggleStore by autoconsentFeatureToggleStore
