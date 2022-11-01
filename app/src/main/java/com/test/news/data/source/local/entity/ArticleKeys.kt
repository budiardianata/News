/*
 * Copyright (C) 2022 Budi Ardianata.
 */
package com.test.news.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "article_keys")
data class ArticleKeys(
    @PrimaryKey val id: String,
    val prevKey: Int?,
    val nextKey: Int?,
)
