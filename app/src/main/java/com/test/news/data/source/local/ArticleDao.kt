/*
 * Copyright (C) 2022 Budi Ardianata.
 */
package com.test.news.data.source.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.test.news.data.source.local.entity.ArticleEntity

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(stories: List<ArticleEntity>): LongArray

    @Query("SELECT * FROM articles")
    fun getNews(): PagingSource<Int, ArticleEntity>

    @Query("DELETE FROM articles")
    fun clearAll()
}
