/*
 * Copyright (C) 2022 Budi Ardianata.
 */
package com.test.news.data.source

import com.test.news.data.source.remote.NewsApi
import com.test.news.data.source.remote.response.NewsResponse
import com.test.news.util.FakeDataProvider
import retrofit2.HttpException
import retrofit2.Response

enum class TestMode {
    SUCCESS,
    ERROR,
    EMPTY
}

internal class FakeRemoteDataSource(private val testMode: TestMode) : NewsApi {

    override suspend fun getArticles(
        page: Int,
        pageSize: Int,
        category: String,
        country: String,
        query: String?,
    ): NewsResponse {
        when (testMode) {
            TestMode.SUCCESS -> {
                val articles = FakeDataProvider.generateArticle(pageSize * 5)
                return NewsResponse(
                    "Success",
                    articles.size,
                    articles.subList(
                        (page - 1) * pageSize,
                        (page - 1) * pageSize + pageSize
                    )
                )
            }
            TestMode.EMPTY -> {
                return NewsResponse("Success", 0, emptyList())
            }
            TestMode.ERROR -> {
                val error = FakeDataProvider.apiError("Test Error")
                throw HttpException(Response.error<NewsResponse>(500, error))
            }
        }
    }
}
