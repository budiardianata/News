/*
 * Copyright (C) 2022 Budi Ardianata.
 */
package com.test.news.util

import javax.inject.Qualifier

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class IODispatcher

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class MainDispatcher

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class IOCoroutineScope

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class BaseUrl
