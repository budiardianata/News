/*
 * Copyright (C) 2022 Budi Ardianata.
 */
package com.test.news.data.mapper

import com.test.news.data.source.local.entity.ArticleEntity
import com.test.news.domain.model.Article
import dagger.Reusable
import javax.inject.Inject

@Reusable
class MapArticleEntityToArticle @Inject constructor() : Mapper<ArticleEntity, Article>() {
    override fun map(input: ArticleEntity): Article {
        return Article(
            id = input.id,
            author = input.author,
            content = input.content,
            description = input.description,
            publishedAt = input.publishedAt,
            source = input.source,
            title = input.title,
            url = input.url,
            urlToImage = input.urlToImage
        )
    }
}
