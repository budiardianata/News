/*
 * Copyright (C) 2022 Budi Ardianata.
 */
package com.test.news.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Article(
    val id: String,
    val title: String,
    val description: String?,
    val content: String?,
    val publishedAt: Long,
    val urlToImage: String?,
    val url: String,
    val author: String?,
    val source: Source,
) : Parcelable
