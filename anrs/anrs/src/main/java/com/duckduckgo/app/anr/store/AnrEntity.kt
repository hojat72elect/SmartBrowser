package com.duckduckgo.app.anr.store

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "anr_entity")
data class AnrEntity(
    @PrimaryKey val hash: String,
    val message: String,
    val name: String,
    val file: String?,
    val lineNumber: Int,
    val stackTrace: List<String>,
    val timestamp: String,
)
