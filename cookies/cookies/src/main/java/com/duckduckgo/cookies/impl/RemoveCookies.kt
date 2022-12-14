/*
 * Copyright (c) 2022 DuckDuckGo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duckduckgo.cookies.impl

import com.duckduckgo.cookies.api.CookieRemover
import com.duckduckgo.cookies.api.RemoveCookiesStrategy
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import javax.inject.Named

@ContributesBinding(AppScope::class)
class RemoveCookies @Inject constructor(
    @Named("cookieManagerRemover") private val cookieManagerRemover: CookieRemover,
    @Named("sqlCookieRemover") private val selectiveCookieRemover: CookieRemover,
) : RemoveCookiesStrategy {
    override suspend fun removeCookies() {
        val removeSuccess = selectiveCookieRemover.removeCookies()
        if (!removeSuccess) {
            cookieManagerRemover.removeCookies()
        }
    }
}
