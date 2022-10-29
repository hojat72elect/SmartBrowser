package com.duckduckgo.privacy.config.store.features.amplinks

import com.duckduckgo.app.CoroutineTestRule
import com.duckduckgo.privacy.config.store.toAmpLinkException
import junit.framework.TestCase.assertEquals
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

class RealAmpLinksRepositoryTest {

    @get:Rule
    var coroutineRule = CoroutineTestRule()

    lateinit var testee: RealAmpLinksRepository

    private val mockDatabase: com.duckduckgo.privacy.config.store.PrivacyConfigDatabase = mock()
    private val mockAmpLinksDao: AmpLinksDao = mock()

    @Before
    fun before() {
        whenever(mockDatabase.ampLinksDao()).thenReturn(mockAmpLinksDao)
        testee = RealAmpLinksRepository(
            mockDatabase,
            TestScope(),
            coroutineRule.testDispatcherProvider
        )
    }

    @Test
    fun whenRepositoryIsCreatedThenValuesLoadedIntoMemory() {
        givenAmpLinksDaoContainsEntities()

        testee = RealAmpLinksRepository(
            mockDatabase,
            TestScope(),
            coroutineRule.testDispatcherProvider
        )

        assertEquals(ampLinkExceptionEntity.toAmpLinkException(), testee.exceptions.first())
        assertEquals(ampLinkFormatEntity.format, testee.ampLinkFormats.first().toString())
        assertEquals(ampKeywordEntity.keyword, testee.ampKeywords.first())
    }

    @Test
    fun whenUpdateAllThenUpdateAllCalled() = runTest {
        testee = RealAmpLinksRepository(
            mockDatabase,
            TestScope(),
            coroutineRule.testDispatcherProvider
        )

        testee.updateAll(listOf(), listOf(), listOf())

        verify(mockAmpLinksDao).updateAll(anyList(), anyList(), anyList())
    }

    @Test
    fun whenUpdateAllThenPreviousValuesAreCleared() = runTest {
        givenAmpLinksDaoContainsEntities()

        testee = RealAmpLinksRepository(
            mockDatabase,
            TestScope(),
            coroutineRule.testDispatcherProvider
        )
        assertEquals(1, testee.exceptions.size)
        assertEquals(1, testee.ampLinkFormats.size)
        assertEquals(1, testee.ampKeywords.size)

        reset(mockAmpLinksDao)

        testee.updateAll(listOf(), listOf(), listOf())

        assertEquals(0, testee.exceptions.size)
        assertEquals(0, testee.ampLinkFormats.size)
        assertEquals(0, testee.ampKeywords.size)
    }

    private fun givenAmpLinksDaoContainsEntities() {
        whenever(mockAmpLinksDao.getAllExceptions()).thenReturn(listOf(ampLinkExceptionEntity))
        whenever(mockAmpLinksDao.getAllAmpLinkFormats()).thenReturn(listOf(ampLinkFormatEntity))
        whenever(mockAmpLinksDao.getAllAmpKeywords()).thenReturn(listOf(ampKeywordEntity))
    }

    companion object {
        val ampLinkExceptionEntity = com.duckduckgo.privacy.config.store.AmpLinkExceptionEntity(
            domain = "https://www.example.com",
            reason = "reason"
        )

        val ampLinkFormatEntity = com.duckduckgo.privacy.config.store.AmpLinkFormatEntity(
            format = "https?:\\/\\/(?:w{3}\\.)?google\\.\\w{2,}\\/amp\\/s\\/(\\S+)"
        )

        val ampKeywordEntity = com.duckduckgo.privacy.config.store.AmpKeywordEntity(
            keyword = "keyword"
        )
    }
}
