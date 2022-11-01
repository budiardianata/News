/*
 * Copyright (C) 2022 Budi Ardianata.
 */
package com.test.news.data.source.remote.response

data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<ArticleResponse>,
)
