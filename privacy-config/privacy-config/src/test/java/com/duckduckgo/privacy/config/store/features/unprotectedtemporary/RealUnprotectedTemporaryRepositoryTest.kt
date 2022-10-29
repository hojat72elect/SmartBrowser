package com.duckduckgo.privacy.config.store.features.unprotectedtemporary

import com.duckduckgo.app.CoroutineTestRule
import com.duckduckgo.privacy.config.store.toUnprotectedTemporaryException
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
class RealUnprotectedTemporaryRepositoryTest {

    @get:Rule
    var coroutineRule = CoroutineTestRule()

    lateinit var testee: RealUnprotectedTemporaryRepository

    private val mockDatabase: com.duckduckgo.privacy.config.store.PrivacyConfigDatabase = mock()
    private val mockUnprotectedTemporaryDao: UnprotectedTemporaryDao = mock()

    @Before
    fun before() {
        whenever(mockDatabase.unprotectedTemporaryDao()).thenReturn(mockUnprotectedTemporaryDao)
        testee =
            RealUnprotectedTemporaryRepository(
                mockDatabase, TestScope(), coroutineRule.testDispatcherProvider
            )
    }

    @Test
    fun whenRepositoryIsCreatedThenExceptionsLoadedIntoMemory() {
        givenUnprotectedTemporaryDaoContainsExceptions()

        testee =
            RealUnprotectedTemporaryRepository(
                mockDatabase, TestScope(), coroutineRule.testDispatcherProvider
            )

        assertEquals(
            unprotectedTemporaryException.toUnprotectedTemporaryException(),
            testee.exceptions.first()
        )
    }

    @Test
    fun whenUpdateAllThenUpdateAllCalled() =
        runTest {
            testee =
                RealUnprotectedTemporaryRepository(
                    mockDatabase, TestScope(), coroutineRule.testDispatcherProvider
                )

            testee.updateAll(listOf())

            verify(mockUnprotectedTemporaryDao).updateAll(ArgumentMatchers.anyList())
        }

    @Test
    fun whenUpdateAllThenPreviousExceptionsAreCleared() =
        runTest {
            givenUnprotectedTemporaryDaoContainsExceptions()
            testee =
                RealUnprotectedTemporaryRepository(
                    mockDatabase, TestScope(), coroutineRule.testDispatcherProvider
                )
            assertEquals(1, testee.exceptions.size)
            reset(mockUnprotectedTemporaryDao)

            testee.updateAll(listOf())

            assertEquals(0, testee.exceptions.size)
        }

    private fun givenUnprotectedTemporaryDaoContainsExceptions() {
        whenever(mockUnprotectedTemporaryDao.getAll())
            .thenReturn(listOf(unprotectedTemporaryException))
    }

    companion object {
        val unprotectedTemporaryException =
            com.duckduckgo.privacy.config.store.UnprotectedTemporaryEntity("example.com", "reason")
    }
}
