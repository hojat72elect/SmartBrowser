package com.duckduckgo.privacy.config.store.features.contentblocking

import com.duckduckgo.app.CoroutineTestRule
import com.duckduckgo.privacy.config.store.toContentBlockingException
import org.mockito.kotlin.mock
import org.mockito.kotlin.reset
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyList

@ExperimentalCoroutinesApi
class RealContentBlockingRepositoryTest {

    @get:Rule
    var coroutineRule = CoroutineTestRule()

    lateinit var testee: RealContentBlockingRepository

    private val mockDatabase: com.duckduckgo.privacy.config.store.PrivacyConfigDatabase = mock()
    private val mockContentBlockingDao: ContentBlockingDao = mock()

    @Before
    fun before() {
        whenever(mockDatabase.contentBlockingDao()).thenReturn(mockContentBlockingDao)
    }

    @Test
    fun whenRepositoryIsCreatedThenExceptionsLoadedIntoMemory() {
        givenContentBlockingDaoContainsExceptions()

        testee =
            RealContentBlockingRepository(
                mockDatabase, TestScope(), coroutineRule.testDispatcherProvider
            )

        assertEquals(
            contentBlockingException.toContentBlockingException(), testee.exceptions.first()
        )
    }

    @Test
    fun whenUpdateAllThenUpdateAllCalled() =
        runTest {
            testee =
                RealContentBlockingRepository(
                    mockDatabase, TestScope(), coroutineRule.testDispatcherProvider
                )

            testee.updateAll(listOf())

            verify(mockContentBlockingDao).updateAll(anyList())
        }

    @Test
    fun whenUpdateAllThenPreviousExceptionsAreCleared() =
        runTest {
            givenContentBlockingDaoContainsExceptions()
            testee =
                RealContentBlockingRepository(
                    mockDatabase, TestScope(), coroutineRule.testDispatcherProvider
                )
            assertEquals(1, testee.exceptions.size)
            reset(mockContentBlockingDao)

            testee.updateAll(listOf())

            assertEquals(0, testee.exceptions.size)
        }

    private fun givenContentBlockingDaoContainsExceptions() {
        whenever(mockContentBlockingDao.getAll()).thenReturn(listOf(contentBlockingException))
    }

    companion object {
        val contentBlockingException =
            com.duckduckgo.privacy.config.store.ContentBlockingExceptionEntity(
                "example.com",
                "my reason here"
            )
    }
}
