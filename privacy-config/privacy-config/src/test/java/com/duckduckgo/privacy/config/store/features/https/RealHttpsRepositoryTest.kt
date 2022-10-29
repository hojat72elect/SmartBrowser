package com.duckduckgo.privacy.config.store.features.https

import com.duckduckgo.app.CoroutineTestRule
import com.duckduckgo.privacy.config.store.toHttpsException
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
import org.mockito.ArgumentMatchers

@ExperimentalCoroutinesApi
class RealHttpsRepositoryTest {

    @get:Rule
    var coroutineRule = CoroutineTestRule()

    lateinit var testee: RealHttpsRepository

    private val mockDatabase: com.duckduckgo.privacy.config.store.PrivacyConfigDatabase = mock()
    private val mockHttpsDao: HttpsDao = mock()

    @Before
    fun before() {
        whenever(mockDatabase.httpsDao()).thenReturn(mockHttpsDao)
        testee =
            RealHttpsRepository(
                mockDatabase, TestScope(), coroutineRule.testDispatcherProvider
            )
    }

    @Test
    fun whenRepositoryIsCreatedThenExceptionsLoadedIntoMemory() {
        givenHttpsDaoContainsExceptions()

        testee =
            RealHttpsRepository(
                mockDatabase, TestScope(), coroutineRule.testDispatcherProvider
            )

        assertEquals(httpException.toHttpsException(), testee.exceptions.first())
    }

    @Test
    fun whenUpdateAllThenUpdateAllCalled() =
        runTest {
            testee =
                RealHttpsRepository(
                    mockDatabase, TestScope(), coroutineRule.testDispatcherProvider
                )

            testee.updateAll(listOf())

            verify(mockHttpsDao).updateAll(ArgumentMatchers.anyList())
        }

    @Test
    fun whenUpdateAllThenPreviousExceptionsAreCleared() =
        runTest {
            givenHttpsDaoContainsExceptions()
            testee =
                RealHttpsRepository(
                    mockDatabase, TestScope(), coroutineRule.testDispatcherProvider
                )
            assertEquals(1, testee.exceptions.size)
            reset(mockHttpsDao)

            testee.updateAll(listOf())

            assertEquals(0, testee.exceptions.size)
        }

    private fun givenHttpsDaoContainsExceptions() {
        whenever(mockHttpsDao.getAll()).thenReturn(listOf(httpException))
    }

    companion object {
        val httpException =
            com.duckduckgo.privacy.config.store.HttpsExceptionEntity("example.com", "reason")
    }
}
