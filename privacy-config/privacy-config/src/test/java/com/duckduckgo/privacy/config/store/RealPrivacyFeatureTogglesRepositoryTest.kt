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

package com.duckduckgo.privacy.config.store

import com.duckduckgo.privacy.config.api.PrivacyFeatureName
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.junit.Before
import org.junit.Test

class RealPrivacyFeatureTogglesRepositoryTest {

    lateinit var testee: com.duckduckgo.privacy.config.store.RealPrivacyFeatureTogglesRepository

    private val mockPrivacyFeatureTogglesDataStore: com.duckduckgo.privacy.config.store.PrivacyFeatureTogglesDataStore = mock()

    @Before
    fun before() {
        testee = com.duckduckgo.privacy.config.store.RealPrivacyFeatureTogglesRepository(
            mockPrivacyFeatureTogglesDataStore
        )
    }

    @Test
    fun whenDeleteAllThenDeleteAllCalled() {
        testee.deleteAll()

        verify(mockPrivacyFeatureTogglesDataStore).deleteAll()
    }

    @Test
    fun whenGetThenGetCalled() {
        testee.get(com.duckduckgo.privacy.config.api.PrivacyFeatureName.GpcFeatureName, true)

        verify(mockPrivacyFeatureTogglesDataStore).get(com.duckduckgo.privacy.config.api.PrivacyFeatureName.GpcFeatureName, true)
    }

    @Test
    fun whenInsertThenInsertCalled() {
        val privacyFeatureToggle = com.duckduckgo.privacy.config.store.PrivacyFeatureToggles(
            com.duckduckgo.privacy.config.api.PrivacyFeatureName.GpcFeatureName.value,
            true,
            null
        )
        testee.insert(privacyFeatureToggle)

        verify(mockPrivacyFeatureTogglesDataStore).insert(privacyFeatureToggle)
    }
}
