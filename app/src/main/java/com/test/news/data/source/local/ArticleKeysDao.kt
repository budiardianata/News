/*
 * Copyright (C) 2022 Budi Ardianata.
 */
package com.test.news.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.test.news.data.source.local.entity.ArticleKeys

@Dao
interface ArticleKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(remoteKey: List<ArticleKeys>): LongArray

    @Query("SELECT * FROM article_keys WHERE id = :id")
    suspend fun getById(id: String): ArticleKeys?

    @Query("DELETE FROM article_keys")
    suspend fun clearAll()
}
