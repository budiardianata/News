/*
 * Copyright (C) 2022 Budi Ardianata.
 */
package com.test.news.domain.repositories

import androidx.paging.PagingData
import com.test.news.domain.model.Article
import kotlinx.coroutines.flow.Flow

interface ArticleRepository {

    fun getPagedArticle(
        query: String? = null,
    ): Flow<PagingData<Article>>
}
