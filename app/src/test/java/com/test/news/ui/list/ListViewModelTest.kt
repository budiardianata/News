/*
 * Copyright (C) 2022 Budi Ardianata.
 */
package com.test.news.ui.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import app.cash.turbine.testIn
import com.test.news.domain.repositories.ArticleRepository
import com.test.news.util.CoroutineDispatcherRule
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
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
}
