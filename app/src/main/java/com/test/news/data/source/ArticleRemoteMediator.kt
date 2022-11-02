/*
 * Copyright (C) 2022 Budi Ardianata.
 */
package com.test.news.data.source

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.google.gson.Gson
import com.test.news.data.mapper.MapArticleResponseToEntity
import com.test.news.data.source.local.NewsDatabase
import com.test.news.data.source.local.entity.ArticleEntity
import com.test.news.data.source.local.entity.ArticleKeys
import com.test.news.data.source.remote.NewsApi
import com.test.news.data.source.remote.response.ErrorResponse
import com.test.news.data.source.remote.response.NewsResponse
import com.test.news.util.Constant
import com.test.news.util.fromJson
import java.io.IOException
import retrofit2.HttpException

@ExperimentalPagingApi
class ArticleRemoteMediator constructor(
    private val query: String?,
    private val newsApi: NewsApi,
    private val newsDatabase: NewsDatabase,
    private val mapper: MapArticleResponseToEntity,
) : RemoteMediator<Int, ArticleEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ArticleEntity>,
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
            val apiResponse: NewsResponse = newsApi.getArticles(
                page = page,
                pageSize = state.config.pageSize,
                query = query
            )
            val dataList = apiResponse.articles
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
        } catch (exception: HttpException) {
            val response = exception.response()
            val error: ErrorResponse = Gson().fromJson(response?.errorBody()?.charStream())
            return MediatorResult.Error(Exception(error.message))
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }
    }

    override suspend fun initialize(): InitializeAction = InitializeAction.LAUNCH_INITIAL_REFRESH

    private suspend fun getRemoteKeysForLastItem(state: PagingState<Int, ArticleEntity>): ArticleKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            newsDatabase.articleKeysDao().getById(data.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, ArticleEntity>): ArticleKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            newsDatabase.articleKeysDao().getById(data.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, ArticleEntity>,
    ): ArticleKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                newsDatabase.articleKeysDao().getById(id)
            }
        }
    }
}
