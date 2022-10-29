package com.duckduckgo.privacy.config.store

import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.junit.Before
import org.junit.Test

class RealPrivacyConfigRepositoryTest {

    lateinit var testee: RealPrivacyConfigRepository

    private val mockDatabase: PrivacyConfigDatabase = mock()
    private val mockPrivacyConfigDao: PrivacyConfigDao = mock()

    @Before
    fun before() {
        whenever(mockDatabase.privacyConfigDao()).thenReturn(mockPrivacyConfigDao)
        testee = RealPrivacyConfigRepository(mockDatabase)
    }

    @Test
    fun whenInsertPrivacyConfigThenCallInsert() {
        val privacyConfig = PrivacyConfig(
            id = 1,
            version = 1,
            readme = "readme"
        )

        testee.insert(privacyConfig)

        verify(mockPrivacyConfigDao).insert(privacyConfig)
    }

    @Test
    fun whenDeleteThenCallDelete() {
        testee.delete()

        verify(mockPrivacyConfigDao).delete()
    }

    @Test
    fun whenGetThenCallGet() {
        testee.get()

        verify(mockPrivacyConfigDao).get()
    }
}
