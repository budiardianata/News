/*
 * Copyright (C) 2022 Budi Ardianata.
 */
package com.test.news.ui.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import app.cash.turbine.testIn
import com.test.news.data.mapper.MapArticleEntityToArticle
import com.test.news.data.mapper.MapArticleResponseToEntity
import com.test.news.domain.repositories.ArticleRepository
import com.test.news.util.CoroutineDispatcherRule
import com.test.news.util.FakeDataProvider
import com.test.news.util.collectData
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ListViewModelTest {
    @get:Rule
    var dispatcherRule = CoroutineDispatcherRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @MockK(relaxed = true)
    private lateinit var repository: ArticleRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `verify initial query`() = runTest {
        val expectedQuery = null
        every { repository.getPagedArticle(any()) } returns flowOf(PagingData.empty())
        val viewModel = ListViewModel(repository)

        val fakeReceiver = viewModel.articlePagedData.testIn(this)

        assertEquals(expectedQuery, viewModel.query.value)
        coVerify { repository.getPagedArticle(expectedQuery) }
        fakeReceiver.cancelAndConsumeRemainingEvents()
    }

    @Test
    fun `perform onQueryChanged - then verify repository call with given query`() = runTest {
        val expectedQuery = "test"
        every { repository.getPagedArticle(any()) } returns flowOf(PagingData.empty())
        val viewModel = ListViewModel(repository)

        val fakeReceiver = viewModel.articlePagedData.testIn(this)
        viewModel.onQueryChanged(expectedQuery)

        assertEquals(expectedQuery, viewModel.query.value)
        coVerify { repository.getPagedArticle(expectedQuery) }
        fakeReceiver.cancelAndConsumeRemainingEvents()
    }

    @Test
    fun `collect articlePagedData with empty result`() = runTest {
        every { repository.getPagedArticle(any()) } returns flowOf(PagingData.empty())
        val viewModel = ListViewModel(repository)

        val actualReceiver = viewModel.articlePagedData.testIn(this)

        val actualResult = actualReceiver.awaitItem().collectData(ListArticleAdapter.DIFF_CALLBACK)
        assertTrue(actualResult.isEmpty())
        verify {
            repository.getPagedArticle(null)
        }
        actualReceiver.cancelAndConsumeRemainingEvents()
    }

    @Test
    fun `collect articlePagedData with success result`() = runTest {
        val listItems = MapArticleResponseToEntity().listMap(FakeDataProvider.generateArticle(10))
        val expected = MapArticleEntityToArticle().listMap(listItems)
        every { repository.getPagedArticle(any()) } returns flowOf(PagingData.from(expected))
        val viewModel = ListViewModel(repository)

        val actualReceiver = viewModel.articlePagedData.testIn(this)

        val actualResult = actualReceiver.awaitItem().collectData(ListArticleAdapter.DIFF_CALLBACK)
        assertNotNull(actualResult)
        assertEquals(actualResult.size, expected.size)
        assertEquals(actualResult, expected)
        verify {
            repository.getPagedArticle(null)
        }
        actualReceiver.cancelAndConsumeRemainingEvents()
    }
}
