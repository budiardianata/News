/*
 * Copyright (C) 2022 Budi Ardianata.
 */
package com.test.news.data.mapper

import com.test.news.data.source.local.entity.ArticleEntity
import com.test.news.domain.model.Article
import com.test.news.domain.model.Source
import org.junit.Assert.*
import org.junit.Test

class MapArticleEntityToArticleTest {

    private val expected = Article(
        id = "author-name",
        author = "author name",
        content = "content",
        description = "description",
        publishedAt = 123456,
        source = Source("id", "name"),
        title = "title",
        url = "url",
        urlToImage = "urlToImage"
    )
    private val input = ArticleEntity(
        id = "author-name",
        author = "author name",
        content = "content",
        description = "description",
        publishedAt = 123456,
        source = Source("id", "name"),
        title = "title",
        url = "url",
        urlToImage = "urlToImage"
    )

    @Test
    fun `verify map`() {
        val mapper = MapArticleEntityToArticle()
        val actual = mapper.map(input)
        assertEquals(expected, actual)
    }

    @Test
    fun `verify listMap`() {
        val mapper = MapArticleEntityToArticle()
        val actual = mapper.listMap(listOf(input))
        assertEquals(listOf(expected), actual)
        assertEquals(listOf(expected).size, actual.size)
    }
}
