package com.duckduckgo.site.permissions.store

import androidx.room.Database
import androidx.room.RoomDatabase
import com.duckduckgo.site.permissions.store.sitepermissions.SitePermissionsDao
import com.duckduckgo.site.permissions.store.sitepermissions.SitePermissionsEntity
import com.duckduckgo.site.permissions.store.sitepermissionsallowed.SitePermissionAllowedEntity
import com.duckduckgo.site.permissions.store.sitepermissionsallowed.SitePermissionsAllowedDao

@Database(
    exportSchema = true,
    version = 1,
    entities = [SitePermissionsEntity::class, SitePermissionAllowedEntity::class]
)

abstract class SitePermissionsDatabase : RoomDatabase() {
    abstract fun sitePermissionsDao(): SitePermissionsDao
    abstract fun sitePermissionsAllowedDao(): SitePermissionsAllowedDao
}
