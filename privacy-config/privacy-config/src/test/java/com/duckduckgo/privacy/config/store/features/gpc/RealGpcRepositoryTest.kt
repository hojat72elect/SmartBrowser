package com.duckduckgo.privacy.config.store.features.gpc

import com.duckduckgo.app.CoroutineTestRule
import com.duckduckgo.privacy.config.store.toGpcException
import org.mockito.kotlin.mock
import org.mockito.kotlin.reset
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyList

@ExperimentalCoroutinesApi
class RealGpcRepositoryTest {
    @get:Rule
    var coroutineRule = CoroutineTestRule()

    lateinit var testee: RealGpcRepository

    private val mockDatabase: com.duckduckgo.privacy.config.store.PrivacyConfigDatabase = mock()
    private val mockGpcExceptionsDao: GpcExceptionsDao = mock()
    private val mockGpcHeadersDao: GpcHeadersDao = mock()
    private val mockGpcContentScopeDao: GpcContentScopeConfigDao = mock()
    private val mockGpcDataStore: GpcDataStore = mock()

    @Before
    fun before() {
        whenever(mockDatabase.gpcHeadersDao()).thenReturn(mockGpcHeadersDao)
        whenever(mockDatabase.gpcExceptionsDao()).thenReturn(mockGpcExceptionsDao)
        whenever(mockDatabase.gpcContentScopeConfigDao()).thenReturn(mockGpcContentScopeDao)
        testee =
            RealGpcRepository(
                mockGpcDataStore,
                mockDatabase,
                TestScope(),
                coroutineRule.testDispatcherProvider
            )
    }

    @Test
    fun whenRepositoryIsCreatedThenExceptionsLoadedIntoMemory() {
        givenGpcDaoContainsExceptions()

        testee =
            RealGpcRepository(
                mockGpcDataStore,
                mockDatabase,
                TestScope(),
                coroutineRule.testDispatcherProvider
            )

        assertEquals(gpcException.toGpcException(), testee.exceptions.first())
    }

    @Test
    fun whenUpdateAllThenUpdateAllCalled() =
        runTest {
            testee =
                RealGpcRepository(
                    mockGpcDataStore,
                    mockDatabase,
                    TestScope(),
                    coroutineRule.testDispatcherProvider
                )

            testee.updateAll(listOf(), listOf(), configEntity)

            verify(mockGpcExceptionsDao).updateAll(anyList())
            verify(mockGpcHeadersDao).updateAll(anyList())
            verify(mockGpcContentScopeDao).insert(configEntity)
        }

    @Test
    fun whenUpdateAllThenPreviousExceptionsAreCleared() =
        runTest {
            givenGpcDaoContainsExceptions()
            testee =
                RealGpcRepository(
                    mockGpcDataStore,
                    mockDatabase,
                    TestScope(),
                    coroutineRule.testDispatcherProvider
                )
            assertEquals(1, testee.exceptions.size)
            reset(mockGpcExceptionsDao)

            testee.updateAll(listOf(), listOf(), configEntity)

            assertEquals(0, testee.exceptions.size)
        }

    @Test
    fun whenUpdateAllThenPreviousHeadersAreCleared() =
        runTest {
            givenGpcDaoContainsHeaders()
            testee =
                RealGpcRepository(
                    mockGpcDataStore,
                    mockDatabase,
                    TestScope(),
                    coroutineRule.testDispatcherProvider
                )
            assertEquals(1, testee.headerEnabledSites.size)
            reset(mockGpcHeadersDao)

            testee.updateAll(listOf(), listOf(), configEntity)

            assertEquals(0, testee.headerEnabledSites.size)
        }

    @Test
    fun whenUpdateAllThenReplaceConfig() =
        runTest {
            givenGpcDaoContainsConfig(configEntity)
            testee =
                RealGpcRepository(
                    mockGpcDataStore,
                    mockDatabase,
                    TestScope(),
                    coroutineRule.testDispatcherProvider
                )
            assertEquals(configEntity.config, testee.gpcContentScopeConfig)
            givenGpcDaoContainsConfig(configEntity2)

            testee.updateAll(listOf(), listOf(), configEntity2)

            assertEquals(configEntity2.config, testee.gpcContentScopeConfig)
        }

    @Test
    fun whenEnableGpcThenSetGpcEnabledToTrue() {
        testee.enableGpc()

        verify(mockGpcDataStore).gpcEnabled = true
    }

    @Test
    fun whenDisableGpcThenSetGpcEnabledToFalse() {
        testee.disableGpc()

        verify(mockGpcDataStore).gpcEnabled = false
    }

    @Test
    fun whenIsGpcEnabledThenReturnGpcEnabledValue() {
        whenever(mockGpcDataStore.gpcEnabled).thenReturn(true)
        assertTrue(testee.isGpcEnabled())

        whenever(mockGpcDataStore.gpcEnabled).thenReturn(false)
        assertFalse(testee.isGpcEnabled())
    }

    private fun givenGpcDaoContainsExceptions() {
        whenever(mockGpcExceptionsDao.getAll()).thenReturn(listOf(gpcException))
    }

    private fun givenGpcDaoContainsHeaders() {
        whenever(mockGpcHeadersDao.getAll()).thenReturn(listOf(gpcHeader))
    }

    private fun givenGpcDaoContainsConfig(configEntity: com.duckduckgo.privacy.config.store.GpcContentScopeConfigEntity) {
        whenever(mockGpcContentScopeDao.getConfig()).thenReturn(configEntity)
    }

    companion object {
        val gpcException = com.duckduckgo.privacy.config.store.GpcExceptionEntity("example.com")
        val gpcHeader =
            com.duckduckgo.privacy.config.store.GpcHeaderEnabledSiteEntity("example.com")
        val configEntity =
            com.duckduckgo.privacy.config.store.GpcContentScopeConfigEntity(config = "{\"feature\":{\"state\":\"enabled\"}}")
        val configEntity2 =
            com.duckduckgo.privacy.config.store.GpcContentScopeConfigEntity(config = "{\"feature\":{\"state\":\"disabled\"}}")
    }
}
