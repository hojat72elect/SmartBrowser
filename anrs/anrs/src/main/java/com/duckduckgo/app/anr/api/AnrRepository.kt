package com.duckduckgo.app.anr.api

interface AnrRepository {
    fun getAllAnrs(): List<Anr>

    fun peekMostRecentAnr(): Anr?

    fun removeMostRecentAnr(): Anr?
}

data class Anr(
    val message: String?,
    val name: String?,
    val file: String?,
    val lineNumber: Int,
    val stackTrace: List<String>,
    val timestamp: String,
)
