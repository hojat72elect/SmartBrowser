package com.duckduckgo.privacy.config.store.features.trackerallowlist

import com.duckduckgo.app.CoroutineTestRule
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
class RealTrackerAllowlistRepositoryTest {

    @get:Rule
    var coroutineRule = CoroutineTestRule()

    lateinit var testee: RealTrackerAllowlistRepository

    private val mockDatabase: com.duckduckgo.privacy.config.store.PrivacyConfigDatabase = mock()
    private val mockTrackerAllowlistDao: TrackerAllowlistDao = mock()

    @Before
    fun before() {
        whenever(mockDatabase.trackerAllowlistDao()).thenReturn(mockTrackerAllowlistDao)
        testee =
            RealTrackerAllowlistRepository(
                mockDatabase, TestScope(), coroutineRule.testDispatcherProvider
            )
    }

    @Test
    fun whenRepositoryIsCreatedThenExceptionsLoadedIntoMemory() {
        givenHttpsDaoContainsExceptions()

        testee =
            RealTrackerAllowlistRepository(
                mockDatabase, TestScope(), coroutineRule.testDispatcherProvider
            )

        assertEquals(trackerAllowlistEntity, testee.exceptions.first())
    }

    @Test
    fun whenUpdateAllThenUpdateAllCalled() =
        runTest {
            testee =
                RealTrackerAllowlistRepository(
                    mockDatabase, TestScope(), coroutineRule.testDispatcherProvider
                )

            testee.updateAll(listOf())

            verify(mockTrackerAllowlistDao).updateAll(anyList())
        }

    @Test
    fun whenUpdateAllThenPreviousExceptionsAreCleared() =
        runTest {
            givenHttpsDaoContainsExceptions()
            testee =
                RealTrackerAllowlistRepository(
                    mockDatabase, TestScope(), coroutineRule.testDispatcherProvider
                )
            assertEquals(1, testee.exceptions.size)
            reset(mockTrackerAllowlistDao)

            testee.updateAll(listOf())

            assertEquals(0, testee.exceptions.size)
        }

    private fun givenHttpsDaoContainsExceptions() {
        whenever(mockTrackerAllowlistDao.getAll()).thenReturn(listOf(trackerAllowlistEntity))
    }

    companion object {
        val trackerAllowlistEntity =
            com.duckduckgo.privacy.config.store.TrackerAllowlistEntity(
                domain = "domain",
                rules =
                listOf(
                    com.duckduckgo.privacy.config.store.AllowlistRuleEntity(
                        rule = "rule", domains = listOf("domain"), reason = "reason"
                    )
                )
            )
    }
}
