/*
 * Copyright (C) 2022 Budi Ardianata.
 */
package com.test.news.di

import android.content.Context
import androidx.room.Room
import com.test.news.data.source.local.ArticleDao
import com.test.news.data.source.local.ArticleKeysDao
import com.test.news.data.source.local.NewsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [LocalModule::class]
)
object LocalModuleTest {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): NewsDatabase {
        return Room.inMemoryDatabaseBuilder(
            context.applicationContext,
            NewsDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @Provides
    fun provideNewsDao(database: NewsDatabase): ArticleDao = database.articleDao()

    @Provides
    fun provideNewsKeysDao(database: NewsDatabase): ArticleKeysDao = database.articleKeysDao()
}
