/*
 * Copyright (C) 2022 Budi Ardianata.
 */
package com.test.news.data.source

import androidx.paging.*
import com.test.news.data.mapper.MapArticleResponseToEntity
import com.test.news.data.source.local.NewsDatabase
import com.test.news.data.source.local.entity.ArticleEntity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
@HiltAndroidTest
class ArticleRemoteMediatorTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var database: NewsDatabase

    @Inject
    lateinit var mapper: MapArticleResponseToEntity

    private val pagingState = PagingState<Int, ArticleEntity>(
        listOf(),
        null,
        PagingConfig(10),
        10
    )

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun refreshPaging_then_returnError_Exception() = runBlocking {
        val fakeApi = FakeRemoteDataSource(TestMode.ERROR)
        val remoteMediator = ArticleRemoteMediator(null, fakeApi, database, mapper)

        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Error)
        assertTrue((result as RemoteMediator.MediatorResult.Error).throwable is java.lang.Exception)
    }

    @Test
    fun refreshPaging_then_returnSuccess_noMoreData() = runBlocking {
        val fakeApi = FakeRemoteDataSource(TestMode.EMPTY)
        val remoteMediator = ArticleRemoteMediator(null, fakeApi, database, mapper)

        val result = remoteMediator.load(LoadType.REFRESH, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @Test
    fun refreshPaging_then_returnSuccess_moreDataIsPresent() = runBlocking {
        val fakeApi = FakeRemoteDataSource(TestMode.SUCCESS)
        val remoteMediator = ArticleRemoteMediator(null, fakeApi, database, mapper)

        val result = remoteMediator.load(LoadType.REFRESH, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @After
    fun tearDown() {
        database.apply {
            clearAllTables()
            close()
        }
    }
}
