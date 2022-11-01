/*
 * Copyright (C) 2022 Budi Ardianata.
 */
package com.test.news.data.source.remote.response

data class ArticleResponse(
    val title: String,
    val description: String?,
    val content: String?,
    val publishedAt: String,
    val urlToImage: String?,
    val url: String,
    val author: String?,
    val source: SourceResponse,
)
