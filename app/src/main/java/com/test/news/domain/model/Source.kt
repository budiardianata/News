/*
 * Copyright (C) 2022 Budi Ardianata.
 */
package com.test.news.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Source(
    val id: String?,
    val name: String,
) : Parcelable
