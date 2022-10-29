package com.duckduckgo.privacy.config.store.features.drm

import com.duckduckgo.app.CoroutineTestRule
import com.duckduckgo.privacy.config.store.toDrmException
import org.mockito.kotlin.mock
import org.mockito.kotlin.reset
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyList

@ExperimentalCoroutinesApi
class RealDrmRepositoryTest {

    @get:Rule
    var coroutineRule = CoroutineTestRule()

    lateinit var testee: RealDrmRepository

    private val mockDatabase: com.duckduckgo.privacy.config.store.PrivacyConfigDatabase = mock()
    private val mockDrmDao: DrmDao = mock()

    @Before
    fun before() {
        whenever(mockDatabase.drmDao()).thenReturn(mockDrmDao)
    }

    @Test
    fun whenRepositoryIsCreatedThenExceptionsLoadedIntoMemory() = runTest {
        givenDrmDaoContainsExceptions()

        testee = RealDrmRepository(mockDatabase, this, coroutineRule.testDispatcherProvider)

        assertEquals(drmException.toDrmException(), testee.exceptions.first())
    }

    @Test
    fun whenUpdateAllThenUpdateAllCalled() = runTest {
        testee = RealDrmRepository(mockDatabase, this, coroutineRule.testDispatcherProvider)

        testee.updateAll(listOf())

        verify(mockDrmDao).updateAll(anyList())
    }

    @Test
    fun whenUpdateAllThenPreviousExceptionsAreCleared() = runTest {
        givenDrmDaoContainsExceptions()
        testee = RealDrmRepository(mockDatabase, this, coroutineRule.testDispatcherProvider)
        assertEquals(1, testee.exceptions.size)
        reset(mockDrmDao)

        testee.updateAll(listOf())

        assertEquals(0, testee.exceptions.size)
    }

    private fun givenDrmDaoContainsExceptions() {
        whenever(mockDrmDao.getAll()).thenReturn(listOf(drmException))
    }

    companion object {
        val drmException =
            com.duckduckgo.privacy.config.store.DrmExceptionEntity("example.com", "reason")
    }
}
