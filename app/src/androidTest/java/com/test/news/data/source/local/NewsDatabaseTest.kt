/*
 * Copyright (C) 2022 Budi Ardianata.
 */
package com.test.news.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.MediumTest
import com.test.news.data.mapper.MapArticleResponseToEntity
import com.test.news.data.source.local.entity.ArticleKeys
import com.test.news.util.FakeDataProvider
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
@HiltAndroidTest
@MediumTest
class NewsDatabaseTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val instantExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var database: NewsDatabase

    @Inject
    lateinit var mapper: MapArticleResponseToEntity

    private lateinit var articleDao: ArticleDao
    private lateinit var articleKeysDao: ArticleKeysDao

    @Before
    fun setUp() {
        hiltRule.inject()
        articleDao = database.articleDao()
        articleKeysDao = database.articleKeysDao()
    }

    @Test
    fun insertData_Success() = runBlocking {
        val given = mapper.listMap(FakeDataProvider.generateArticle(5))
        val keys = given.map {
            ArticleKeys(id = it.id, prevKey = 0, nextKey = 1)
        }

        val insertedKey = articleKeysDao.insert(keys)
        val insertedArticle = articleDao.insert(given)

        assertEquals(given.size, insertedKey.size)
        assertEquals(given.size, insertedArticle.size)
        assertEquals(insertedKey.size, insertedArticle.size)
    }

    @After
    fun tearDown() {
        database.run {
            clearAllTables()
            close()
        }
    }
}
