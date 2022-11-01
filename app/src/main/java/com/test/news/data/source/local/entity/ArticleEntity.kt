/*
 * Copyright (C) 2022 Budi Ardianata.
 */
package com.test.news.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.test.news.domain.model.Source

@Entity(tableName = "articles")
data class ArticleEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String?,
    val content: String?,
    val publishedAt: Long,
    val urlToImage: String?,
    val url: String,
    val author: String?,
    val source: Source,
)
