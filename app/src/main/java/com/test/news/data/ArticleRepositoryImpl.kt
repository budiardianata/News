/*
 * Copyright (C) 2022 Budi Ardianata.
 */
package com.test.news.data

import androidx.paging.*
import com.test.news.data.mapper.MapArticleEntityToArticle
import com.test.news.data.mapper.MapArticleResponseToEntity
import com.test.news.data.source.ArticleRemoteMediator
import com.test.news.data.source.local.NewsDatabase
import com.test.news.data.source.remote.NewsApi
import com.test.news.domain.model.Article
import com.test.news.domain.repositories.ArticleRepository
import com.test.news.util.Constant
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Singleton
class ArticleRepositoryImpl @Inject constructor(
    private val database: NewsDatabase,
    private val newsApi: NewsApi,
    private val entityMapper: MapArticleResponseToEntity,
    private val domainMapper: MapArticleEntityToArticle,
) : ArticleRepository {

    @ExperimentalPagingApi
    override fun getPagedArticle(
        query: String?,
    ): Flow<PagingData<Article>> {
        val dbQuery = if (!query.isNullOrEmpty()) "%${query.replace(' ', '%')}%" else null
        return Pager(
            config = PagingConfig(
                pageSize = Constant.PAGING_PAGE_SIZE
            ),
            remoteMediator = ArticleRemoteMediator(
                query,
                newsApi,
                database,
                entityMapper
            ),
            pagingSourceFactory = { database.articleDao().getArticles(dbQuery) }
        ).flow.map { pagingData ->
            pagingData.map(domainMapper::map)
        }
    }
}
