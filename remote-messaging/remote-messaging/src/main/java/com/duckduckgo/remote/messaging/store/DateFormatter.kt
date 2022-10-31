package com.duckduckgo.remote.messaging.store

import org.threeten.bp.format.DateTimeFormatter

internal fun databaseTimestampFormatter() = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
