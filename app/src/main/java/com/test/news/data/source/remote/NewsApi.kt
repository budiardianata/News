/*
 * Copyright (C) 2022 Budi Ardianata.
 */
package com.test.news.data.source.remote

import com.test.news.data.source.remote.response.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("top-headlines")
    suspend fun getArticles(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int,
        @Query("category") category: String = "business",
        @Query("country") country: String = "us",
        @Query("q") query: String? = null,
    ): NewsResponse
}
