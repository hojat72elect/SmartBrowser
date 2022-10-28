package com.duckduckgo.cookies.impl.di

import android.content.Context
import androidx.room.Room
import com.duckduckgo.app.di.AppCoroutineScope
import com.duckduckgo.app.global.DispatcherProvider
import com.duckduckgo.cookies.store.ALL_MIGRATIONS
import com.duckduckgo.cookies.store.CookiesDatabase
import com.duckduckgo.cookies.store.CookiesFeatureToggleRepository
import com.duckduckgo.cookies.store.CookiesFeatureToggleStore
import com.duckduckgo.cookies.store.CookiesRepository
import com.duckduckgo.cookies.store.RealCookieRepository
import com.duckduckgo.cookies.store.RealCookiesFeatureToggleRepository
import com.duckduckgo.cookies.store.RealCookiesFeatureToggleStore
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import dagger.SingleInstanceIn
import kotlinx.coroutines.CoroutineScope

@Module
@ContributesTo(AppScope::class)
object CookiesModule {

    @SingleInstanceIn(AppScope::class)
    @Provides
    fun provideCookiesDatabase(context: Context): CookiesDatabase {
        return Room.databaseBuilder(context, CookiesDatabase::class.java, "cookies.db")
            .enableMultiInstanceInvalidation()
            .fallbackToDestructiveMigration()
            .addMigrations(*ALL_MIGRATIONS)
            .build()
    }

    @SingleInstanceIn(AppScope::class)
    @Provides
    fun provideCookiesRepository(
        database: CookiesDatabase,
        @AppCoroutineScope coroutineScope: CoroutineScope,
        dispatcherProvider: DispatcherProvider
    ): CookiesRepository {
        return RealCookieRepository(database, coroutineScope, dispatcherProvider)
    }

    @SingleInstanceIn(AppScope::class)
    @Provides
    fun provideCookiesFeatureToggleRepository(cookiesFeatureToggleStore: CookiesFeatureToggleStore): CookiesFeatureToggleRepository {
        return RealCookiesFeatureToggleRepository(cookiesFeatureToggleStore)
    }

    @SingleInstanceIn(AppScope::class)
    @Provides
    fun provideCookiesFeatureToggleStore(context: Context): CookiesFeatureToggleStore {
        return RealCookiesFeatureToggleStore(context)
    }
}
