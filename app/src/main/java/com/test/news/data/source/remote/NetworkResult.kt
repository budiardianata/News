/*
 * Copyright (C) 2022 Budi Ardianata.
 */
package com.test.news.data.source.remote

sealed class NetworkResult<T : Any> {
    class Success<T : Any>(val data: T) : NetworkResult<T>()
    class Error<T : Any>(val code: Int, val message: String?) : NetworkResult<T>()
    class Exception<T : Any>(val e: java.lang.Exception) : NetworkResult<T>()
}
