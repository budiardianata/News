/*
 * Copyright (C) 2022 Budi Ardianata.
 */
package com.test.news.data.mapper

import com.test.news.data.source.local.entity.ArticleEntity
import com.test.news.data.source.remote.response.ArticleResponse
import com.test.news.domain.model.Source
import dagger.Reusable
import java.time.Instant
import javax.inject.Inject

@Reusable
class MapArticleResponseToEntity @Inject constructor() : Mapper<ArticleResponse, ArticleEntity>() {
    override fun map(input: ArticleResponse): ArticleEntity {
        val source = Source(
            id = input.source.id,
            name = input.source.name
        )
        val id = input.title.replace(" ", "-").padStart(10)
        return ArticleEntity(
            id = id,
            author = input.author,
            title = input.title,
            description = input.description,
            url = input.url,
            urlToImage = input.urlToImage,
            publishedAt = Instant.parse(input.publishedAt).toEpochMilli(),
            content = input.content,
            source = source
        )
    }
}
