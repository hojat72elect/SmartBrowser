package com.duckduckgo.privacy.config.store.features.trackingparameters

import com.duckduckgo.app.CoroutineTestRule
import com.duckduckgo.privacy.config.store.toTrackingParameterException
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyList
import org.mockito.kotlin.mock
import org.mockito.kotlin.reset
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class RealTrackingParametersRepositoryTest {

    @get:Rule
    var coroutineRule = CoroutineTestRule()

    lateinit var testee: RealTrackingParametersRepository

    private val mockDatabase: com.duckduckgo.privacy.config.store.PrivacyConfigDatabase = mock()
    private val mockTrackingParametersDao: TrackingParametersDao = mock()

    @Before
    fun before() {
        whenever(mockDatabase.trackingParametersDao()).thenReturn(mockTrackingParametersDao)
        testee = RealTrackingParametersRepository(
            mockDatabase,
            TestScope(),
            coroutineRule.testDispatcherProvider
        )
    }

    @Test
    fun whenRepositoryIsCreatedThenValuesLoadedIntoMemory() {
        givenTrackingParametersDaoContainsEntities()

        testee = RealTrackingParametersRepository(
            mockDatabase,
            TestScope(),
            coroutineRule.testDispatcherProvider
        )

        assertEquals(
            trackingParameterExceptionEntity.toTrackingParameterException(),
            testee.exceptions.first()
        )
        assertEquals(trackingParameterEntity.parameter, testee.parameters.first().toString())
    }

    @Test
    fun whenUpdateAllThenUpdateAllCalled() = runTest {
        testee = RealTrackingParametersRepository(
            mockDatabase,
            TestScope(),
            coroutineRule.testDispatcherProvider
        )

        testee.updateAll(listOf(), listOf())

        verify(mockTrackingParametersDao).updateAll(anyList(), anyList())
    }

    @Test
    fun whenUpdateAllThenPreviousValuesAreCleared() = runTest {
        givenTrackingParametersDaoContainsEntities()

        testee = RealTrackingParametersRepository(
            mockDatabase,
            TestScope(),
            coroutineRule.testDispatcherProvider
        )
        assertEquals(1, testee.exceptions.size)
        assertEquals(1, testee.parameters.size)

        reset(mockTrackingParametersDao)

        testee.updateAll(listOf(), listOf())

        assertEquals(0, testee.exceptions.size)
        assertEquals(0, testee.parameters.size)
    }

    private fun givenTrackingParametersDaoContainsEntities() {
        whenever(mockTrackingParametersDao.getAllExceptions()).thenReturn(
            listOf(
                trackingParameterExceptionEntity
            )
        )
        whenever(mockTrackingParametersDao.getAllTrackingParameters()).thenReturn(
            listOf(
                trackingParameterEntity
            )
        )
    }

    companion object {
        val trackingParameterExceptionEntity =
            com.duckduckgo.privacy.config.store.TrackingParameterExceptionEntity(
                domain = "https://www.example.com",
                reason = "reason"
            )

        val trackingParameterEntity = com.duckduckgo.privacy.config.store.TrackingParameterEntity(
            parameter = "parameter"
        )
    }
}
