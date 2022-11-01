/*
 * Copyright (C) 2022 Budi Ardianata.
 */
package com.test.news.di

import com.test.news.util.BaseUrl
import com.test.news.util.Constant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UrlModule {
    @Provides
    @BaseUrl
    fun providesBaseUrl(): String = Constant.BASE_URL
}
