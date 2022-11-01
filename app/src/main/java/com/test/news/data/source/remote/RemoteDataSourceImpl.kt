/*
 * Copyright (C) 2022 Budi Ardianata.
 */
package com.test.news.data.source.remote

import com.google.gson.Gson
import com.test.news.data.source.RemoteDataSource
import com.test.news.data.source.remote.response.ErrorResponse
import com.test.news.data.source.remote.response.NewsResponse
import com.test.news.util.IODispatcher
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.http.Query

@Singleton
class RemoteDataSourceImpl @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    private val newsApi: NewsApi,
) : RemoteDataSource {
    override suspend fun getNews(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int,
        @Query("category") category: String,
        @Query("country") country: String,
        @Query("q") query: String?,
    ): NetworkResult<NewsResponse> = withContext(ioDispatcher) {
        try {
            val response = newsApi.getNews(page, pageSize, category, country, query)
            val body = response.body()
            if (response.isSuccessful && body != null) {
                return@withContext NetworkResult.Success(body)
            }
            val code = response.code()
            val error =
                Gson().fromJson(response.errorBody()?.charStream(), ErrorResponse::class.java)
            return@withContext NetworkResult.Error(code, error.message)
        } catch (e: Exception) {
            return@withContext NetworkResult.Exception(e)
        }
    }
}
