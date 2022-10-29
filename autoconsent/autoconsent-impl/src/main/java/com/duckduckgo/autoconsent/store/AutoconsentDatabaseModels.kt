package com.duckduckgo.autoconsent.store

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "autoconsent_exceptions")
data class AutoconsentExceptionEntity(
    @PrimaryKey val domain: String,
    val reason: String
)

@Entity(tableName = "autoconsent_disabled_cmps")
data class DisabledCmpsEntity(
    @PrimaryKey val name: String,
)
