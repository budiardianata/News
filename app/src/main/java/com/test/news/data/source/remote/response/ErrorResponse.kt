/*
 * Copyright (C) 2022 Budi Ardianata.
 */
package com.test.news.data.source.remote.response

data class ErrorResponse(
    val status: String,
    val code: String,
    val message: String,
)
