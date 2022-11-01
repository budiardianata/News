/*
 * Copyright (C) 2022 Budi Ardianata.
 */
package com.test.news.data.source.remote

import com.test.news.util.CoroutineDispatcherRule
import com.test.news.util.FakeDataProvider
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerifyAll
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class RemoteDataSourceImplTest {
    @get:Rule
    var dispatcherRule = CoroutineDispatcherRule()

    @MockK(relaxed = true)
    lateinit var newsApi: NewsApi

    // SUT
    private lateinit var remoteDataSource: RemoteDataSourceImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        remoteDataSource = RemoteDataSourceImpl(dispatcherRule.testDispatcher, newsApi)
    }

    @Test
    fun `get news - result Error `() = runTest {
        val expectedResult = "This is Fake Error"
        coEvery {
            newsApi.getNews(any(), any(), any(), any(), any())
        } returns Response.error(402, FakeDataProvider.apiError(expectedResult))

        val result = remoteDataSource.getNews(1, 5)
        assertTrue(result is NetworkResult.Error)
        assertEquals(expectedResult, (result as NetworkResult.Error).message)
        coVerifyAll {
            newsApi.getNews(1, 5)
        }
    }

    @Test
    fun `get news - result Success `() = runTest {
        val expectedResult = FakeDataProvider.newsResponse(5)
        coEvery {
            newsApi.getNews(any(), any(), any(), any(), any())
        } returns Response.success(expectedResult)

        val result = remoteDataSource.getNews(1, 5)

        assertTrue(result is NetworkResult.Success)
        assertEquals(expectedResult, (result as NetworkResult.Success).data)
        coVerifyAll {
            newsApi.getNews(1, 5)
        }
    }
}
