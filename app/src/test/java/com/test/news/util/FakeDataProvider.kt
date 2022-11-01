/*
 * Copyright (C) 2022 Budi Ardianata.
 */
package com.test.news.util

import com.test.news.data.source.remote.response.ArticleResponse
import com.test.news.data.source.remote.response.NewsResponse
import com.test.news.data.source.remote.response.SourceResponse
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody

object FakeDataProvider {
    fun apiError(message: String): ResponseBody {
        return "{'status':'error','code':'apiKeyMissing','message':'$message'}".toResponseBody()
    }

    fun newsResponse(max: Int): NewsResponse {
        val articles = mutableListOf<ArticleResponse>()
        for (i in 1..max) {
            val source = SourceResponse(
                id = "id$i",
                name = "name$i"
            )
            val article = ArticleResponse(
                source = source,
                author = "author$i",
                title = "title$i",
                description = "description$i",
                url = "url$i",
                urlToImage = "urlToImage$i",
                publishedAt = "publishedAt$i",
                content = "content$i"
            )
            articles.add(article)
        }
        return NewsResponse(
            status = "ok",
            totalResults = max,
            articles = articles
        )
    }
}
