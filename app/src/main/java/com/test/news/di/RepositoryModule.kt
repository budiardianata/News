/*
 * Copyright (C) 2022 Budi Ardianata.
 */
package com.test.news.di

import com.test.news.data.ArticleRepositoryImpl
import com.test.news.domain.repositories.ArticleRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    @Singleton
    fun bindArticleRepository(repository: ArticleRepositoryImpl): ArticleRepository
}
