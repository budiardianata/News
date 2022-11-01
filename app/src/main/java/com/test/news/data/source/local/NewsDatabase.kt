/*
 * Copyright (C) 2022 Budi Ardianata.
 */
package com.test.news.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.test.news.data.source.local.converter.SourceConverter
import com.test.news.data.source.local.entity.ArticleEntity
import com.test.news.data.source.local.entity.ArticleKeys

@Database(
    entities = [ArticleEntity::class, ArticleKeys::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(SourceConverter::class)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao
    abstract fun articleKeysDao(): ArticleKeysDao
}
