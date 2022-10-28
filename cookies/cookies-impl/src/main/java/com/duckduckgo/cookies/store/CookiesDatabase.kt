package com.duckduckgo.cookies.store

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration

@Database(
    exportSchema = true,
    version = 1,
    entities = [
        FirstPartyCookiePolicyEntity::class,
        CookieExceptionEntity::class
    ]
)
abstract class CookiesDatabase : RoomDatabase() {
    abstract fun cookiesDao(): CookiesDao
}

val ALL_MIGRATIONS = emptyArray<Migration>()
