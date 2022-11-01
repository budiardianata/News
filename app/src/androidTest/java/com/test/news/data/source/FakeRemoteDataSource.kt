/*
 * Copyright (C) 2022 Budi Ardianata.
 */
package com.test.news.data.source

import com.test.news.data.source.remote.NetworkResult
import com.test.news.data.source.remote.response.NewsResponse
import com.test.news.util.FakeDataProvider
import java.io.IOException

enum class TestMode {
    SUCCESS,
    ERROR,
    EMPTY
}

internal class FakeRemoteDataSource(private val testMode: TestMode) : RemoteDataSource {
    override suspend fun getNews(
        page: Int,
        pageSize: Int,
        category: String,
        country: String,
        query: String?,
    ): NetworkResult<NewsResponse> {
        return when (testMode) {
            TestMode.SUCCESS -> {
                val articles = FakeDataProvider.generateArticle(pageSize * 5)
                NetworkResult.Success(
                    NewsResponse(
                        "Success",
                        articles.size,
                        articles.subList(
                            (page - 1) * pageSize,
                            (page - 1) * pageSize + pageSize
                        )
                    )
                )
            }
            TestMode.EMPTY -> NetworkResult.Success(
                NewsResponse("Success", 0, emptyList())
            )
            TestMode.ERROR -> NetworkResult.Exception(IOException())
        }
    }
}
