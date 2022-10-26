/*
 * Copyright (c) 2021 DuckDuckGo
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

package com.duckduckgo.mobile.android.vpn.blocklist

import androidx.annotation.WorkerThread
import com.duckduckgo.app.global.extensions.extractETag
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.mobile.android.vpn.trackers.AppTracker
import com.duckduckgo.mobile.android.vpn.trackers.AppTrackerJsonParser
import com.duckduckgo.mobile.android.vpn.trackers.AppTrackerPackage
import com.duckduckgo.mobile.android.vpn.trackers.JsonAppBlockingList
import com.duckduckgo.mobile.android.vpn.trackers.AppTrackerExcludedPackage
import com.duckduckgo.mobile.android.vpn.trackers.AppTrackerExceptionRule
import com.duckduckgo.mobile.android.vpn.trackers.AppTrackerSystemAppOverridePackage
import com.duckduckgo.mobile.android.vpn.trackers.*
import com.squareup.anvil.annotations.ContributesBinding
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

data class AppTrackerBlocklist(
    val etag: ETag = ETag.InvalidETag,
    val blocklist: List<AppTracker> = listOf(),
    val appPackages: List<AppTrackerPackage> = listOf(),
    val entities: List<AppTrackerEntity> = listOf()
)

sealed class ETag {
    object InvalidETag : ETag()
    data class ValidETag(val value: String) : ETag()
}

interface AppTrackerListDownloader {
    @WorkerThread
    fun downloadAppTrackerBlocklist(): AppTrackerBlocklist
}

@ContributesBinding(AppScope::class)
class RealAppTrackerListDownloader @Inject constructor(
    private val appTrackerListService: AppTrackerListService
) : AppTrackerListDownloader {
    override fun downloadAppTrackerBlocklist(): AppTrackerBlocklist {
        Timber.d("Downloading the app tracker blocklist...")
        val response = runCatching {
            appTrackerListService.appTrackerBlocklist().execute()
        }.getOrElse {
            Timber.w("Error downloading tracker rules list: $it")
            Response.error(400, "".toResponseBody(null))
        }

        if (!response.isSuccessful) {
            Timber.e("Fail to download the app tracker blocklist, error code: ${response.code()}")
            return AppTrackerBlocklist()
        }

        val eTag = response.headers().extractETag()
        val responseBody = response.body()

        val blocklist = extractBlocklist(responseBody)
        val packages = extractAppPackages(responseBody)
        val trackerEntities = extractTrackerEntities(responseBody)

        Timber.d(
            "Received the app tracker remote lists. blocklist size: ${blocklist.size}, " +
                "app-packages size: ${packages.size}, entities size: ${trackerEntities.size}"
        )

        return AppTrackerBlocklist(etag = ETag.ValidETag(eTag), blocklist = blocklist, appPackages = packages, entities = trackerEntities)
    }

    private fun extractBlocklist(response: JsonAppBlockingList?): List<AppTracker> {
        return AppTrackerJsonParser.parseAppTrackers(response)
    }

    private fun extractAppPackages(response: JsonAppBlockingList?): List<AppTrackerPackage> {
        return AppTrackerJsonParser.parseAppPackages(response)
    }

    private fun extractTrackerEntities(response: JsonAppBlockingList?): List<AppTrackerEntity> {
        return AppTrackerJsonParser.parseTrackerEntities(response)
    }
}
