package com.duckduckgo.cookies.store

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.duckduckgo.cookies.api.CookieException

@Entity(tableName = "first_party_cookie_policy")
data class FirstPartyCookiePolicyEntity(
    @PrimaryKey val id: Int = 1,
    val threshold: Int,
    val maxAge: Int,
)

@Entity(tableName = "cookie_exceptions")
data class CookieExceptionEntity(
    @PrimaryKey val domain: String,
    val reason: String
)

fun CookieExceptionEntity.toCookieException(): CookieException {
    return CookieException(domain = this.domain, reason = this.reason)
}
