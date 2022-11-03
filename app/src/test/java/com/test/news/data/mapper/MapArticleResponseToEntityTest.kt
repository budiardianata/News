/*
 * Copyright (C) 2022 Budi Ardianata.
 */
package com.test.news.data.mapper

import com.test.news.data.source.local.entity.ArticleEntity
import com.test.news.data.source.remote.response.ArticleResponse
import com.test.news.data.source.remote.response.SourceResponse
import com.test.news.domain.model.Source
import org.junit.Assert.*
import org.junit.Test

class MapArticleResponseToEntityTest {
    private val input = ArticleResponse(
        author = "author name",
        content = "content",
        description = "description",
        publishedAt = "2021-07-01T12:00:00Z",
        source = SourceResponse("id", "name"),
        title = "title of mapping",
        url = "url",
        urlToImage = "urlToImage"
    )

    private val expected = ArticleEntity(
        id = "title-of-mapping",
        author = "author name",
        content = "content",
        description = "description",
        publishedAt = 1625140800000,
        source = Source("id", "name"),
        title = "title of mapping",
        url = "url",
        urlToImage = "urlToImage"
    )

    @Test
    fun `verify map`() {
        val mapper = MapArticleResponseToEntity()
        val actual = mapper.map(input)
        assertEquals(expected, actual)
    }

    @Test
    fun `verify listMap`() {
        val mapper = MapArticleResponseToEntity()
        val actual = mapper.listMap(listOf(input))
        assertEquals(listOf(expected), actual)
        assertEquals(listOf(expected).size, actual.size)
    }
}
