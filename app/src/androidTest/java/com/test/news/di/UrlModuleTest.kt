/*
 * Copyright (C) 2022 Budi Ardianata.
 */
package com.test.news.di

import com.test.news.util.BaseUrl
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [UrlModule::class]
)
object UrlModuleTest {
    @Provides
    @BaseUrl
    fun providesBaseUrl(): String = "http://127.0.0.1:8080/"
}
