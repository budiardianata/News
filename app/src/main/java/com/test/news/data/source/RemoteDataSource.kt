/*
 * Copyright (C) 2022 Budi Ardianata.
 */
package com.test.news.data.source

import com.test.news.data.source.remote.NetworkResult
import com.test.news.data.source.remote.response.NewsResponse
import retrofit2.http.Query

interface RemoteDataSource {
    suspend fun getNews(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int,
        @Query("category") category: String = "business",
        @Query("country") country: String = "us",
        @Query("q") query: String? = null,
    ): NetworkResult<NewsResponse>
}
