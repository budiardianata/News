/*
 * Copyright (C) 2022 Budi Ardianata.
 */
package com.test.news.util

import com.test.news.data.source.remote.response.ArticleResponse
import com.test.news.data.source.remote.response.SourceResponse
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody

object FakeDataProvider {
    fun apiError(message: String): ResponseBody {
        return "{'status':'error','code':'apiKeyMissing','message':'$message'}".toResponseBody()
    }

    fun generateArticle(max: Int): List<ArticleResponse> {
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
                publishedAt = "2022-11-01T15:53:40Z",
                content = "content$i"
            )
            articles.add(article)
        }
        return articles.toList()
    }
}
