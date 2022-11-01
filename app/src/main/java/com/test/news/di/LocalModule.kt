/*
 * Copyright (C) 2022 Budi Ardianata.
 */
package com.test.news.di

import android.content.Context
import androidx.room.Room
import com.test.news.data.source.local.ArticleDao
import com.test.news.data.source.local.ArticleKeysDao
import com.test.news.data.source.local.NewsDatabase
import com.test.news.util.Constant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): NewsDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            NewsDatabase::class.java,
            Constant.DB_NAME
        ).build()
    }

    @Provides
    fun provideNewsDao(database: NewsDatabase): ArticleDao = database.articleDao()

    @Provides
    fun provideNewsKeysDao(database: NewsDatabase): ArticleKeysDao = database.articleKeysDao()
}
