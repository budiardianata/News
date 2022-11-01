/*
 * Copyright (C) 2022 Budi Ardianata.
 */
package com.test.news.data.source

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.test.news.data.mapper.MapArticleResponseToEntity
import com.test.news.data.source.local.NewsDatabase
import com.test.news.data.source.local.entity.ArticleKeys
import com.test.news.data.source.remote.NetworkResult
import com.test.news.domain.model.Article
import com.test.news.util.Constant
import java.io.IOException
import javax.inject.Inject
import retrofit2.HttpException

@ExperimentalPagingApi
class ArticleRemoteMediator @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val newsDatabase: NewsDatabase,
    private val mapper: MapArticleResponseToEntity,
) : RemoteMediator<Int, Article>() {

    override suspend fun load(
        loadType: LoadType,
        state:
            PagingState<Int, Article>,
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 1
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKeys != null
                )
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeysForLastItem(state)
                val nextKey = remoteKeys?.nextKey ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKeys != null
                )
                nextKey
            }
        }
        try {
            when (
                val apiResponse =
                    remoteDataSource.getNews(page = page, pageSize = state.config.pageSize)
            ) {
                is NetworkResult.Error -> return MediatorResult.Error(Throwable(apiResponse.message))
                is NetworkResult.Exception -> return MediatorResult.Error(apiResponse.e)
                else -> {
                    val dataList = (apiResponse as NetworkResult.Success).data.articles
                    val endOfPaginationReached = dataList.isEmpty()
                    val data = mapper.listMap(dataList)
                    newsDatabase.withTransaction {
                        // clear all tables in the database
                        if (loadType == LoadType.REFRESH) {
                            newsDatabase.articleKeysDao().clearAll()
                            newsDatabase.articleDao().clearAll()
                        }
                        val prevKey = if (page == Constant.PAGING_INITIAL_PAGE) null else page - 1
                        val nextKey = if (endOfPaginationReached) null else page + 1
                        val keys = data.map {
                            ArticleKeys(id = it.id, prevKey = prevKey, nextKey = nextKey)
                        }
                        newsDatabase.articleKeysDao().insert(keys)
                        newsDatabase.articleDao().insert(data)
                    }
                    return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
                }
            }
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    override suspend fun initialize(): InitializeAction = InitializeAction.LAUNCH_INITIAL_REFRESH

    private suspend fun getRemoteKeysForLastItem(state: PagingState<Int, Article>): ArticleKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            newsDatabase.articleKeysDao().getById(data.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Article>): ArticleKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            newsDatabase.articleKeysDao().getById(data.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Article>,
    ): ArticleKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                newsDatabase.articleKeysDao().getById(id)
            }
        }
    }
}
