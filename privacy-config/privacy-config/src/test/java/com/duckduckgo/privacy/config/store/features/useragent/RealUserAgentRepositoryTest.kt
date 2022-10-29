package com.duckduckgo.privacy.config.store.features.useragent

import com.duckduckgo.app.CoroutineTestRule
import com.duckduckgo.privacy.config.store.toUserAgentException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyList
import org.mockito.kotlin.mock
import org.mockito.kotlin.reset
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class RealUserAgentRepositoryTest {

    @get:Rule
    var coroutineRule = CoroutineTestRule()

    lateinit var testee: RealUserAgentRepository

    private val mockDatabase: com.duckduckgo.privacy.config.store.PrivacyConfigDatabase = mock()
    private val mockUserAgentDao: UserAgentDao = mock()

    @Before
    fun before() {
        whenever(mockDatabase.userAgentDao()).thenReturn(mockUserAgentDao)
        testee =
            RealUserAgentRepository(
                mockDatabase, TestScope(), coroutineRule.testDispatcherProvider
            )
    }

    @Test
    fun whenRepositoryIsCreatedThenExceptionsLoadedIntoMemory() {
        givenUserAgentDaoContainsExceptions()
        val actual = userAgentException.toUserAgentException()
        testee =
            RealUserAgentRepository(
                mockDatabase, TestScope(), coroutineRule.testDispatcherProvider
            )

        assertEquals(testee.omitApplicationExceptions.first(), actual)
        assertEquals(testee.omitVersionExceptions.first(), actual)
        assertEquals(testee.defaultExceptions.first(), actual)
    }

    @Test
    fun whenUpdateAllThenUpdateAllCalled() =
        runTest {
            testee =
                RealUserAgentRepository(
                    mockDatabase, TestScope(), coroutineRule.testDispatcherProvider
                )

            testee.updateAll(listOf())

            verify(mockUserAgentDao).updateAll(anyList())
        }

    @Test
    fun whenUpdateAllThenPreviousExceptionsAreCleared() =
        runTest {
            givenUserAgentDaoContainsExceptions()
            testee =
                RealUserAgentRepository(
                    mockDatabase, TestScope(), coroutineRule.testDispatcherProvider
                )
            assertEquals(1, testee.defaultExceptions.size)
            assertEquals(1, testee.omitApplicationExceptions.size)
            assertEquals(1, testee.omitVersionExceptions.size)
            reset(mockUserAgentDao)

            testee.updateAll(listOf())

            assertEquals(0, testee.defaultExceptions.size)
            assertEquals(0, testee.omitApplicationExceptions.size)
            assertEquals(0, testee.omitVersionExceptions.size)
        }

    private fun givenUserAgentDaoContainsExceptions() {
        whenever(mockUserAgentDao.getApplicationExceptions()).thenReturn(listOf(userAgentException))
        whenever(mockUserAgentDao.getDefaultExceptions()).thenReturn(listOf(userAgentException))
        whenever(mockUserAgentDao.getVersionExceptions()).thenReturn(listOf(userAgentException))
    }

    companion object {
        val userAgentException = com.duckduckgo.privacy.config.store.UserAgentExceptionEntity(
            "example.com",
            "reason",
            omitApplication = false,
            omitVersion = false
        )
    }
}
