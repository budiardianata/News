/*
 * Copyright (C) 2022 Budi Ardianata.
 */
package com.test.news.ui.list

import androidx.recyclerview.widget.RecyclerView
import androidx.room.withTransaction
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.MediumTest
import com.test.news.R
import com.test.news.data.mapper.MapArticleResponseToEntity
import com.test.news.data.source.local.NewsDatabase
import com.test.news.data.source.local.entity.ArticleKeys
import com.test.news.util.*
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import javax.inject.Inject
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@MediumTest
@HiltAndroidTest
class ListFragmentTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var database: NewsDatabase

    @Inject
    lateinit var mapper: MapArticleResponseToEntity

    private val mockWebServer = MockWebServer()

    @Before
    fun setUp() {
        hiltRule.inject()
        mockWebServer.start(8080)
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        launchFragmentInHiltContainer<ListFragment>()
    }

    @Test
    fun launchFragment_Failed_showError_when_no_cache() {
        val mockResponse = MockResponse().withResponse("error.json", 400)

        mockWebServer.enqueue(mockResponse)

        onView(withId(R.id.list_article))
            .check(matches(isDisplayed())) // check if recyclerview is displayed
            .check(matches(atPosition(0, hasDescendant(withId(R.id.retry_button_item))))) // check if retry button is displayed
            .check(matches(itemOfRecyclerEqualWith(1))) // check if recyclerview item is equal with 1
    }

    @Test
    fun launchFragment_with_ServerError_then_retrieveData_fromCache(): Unit = runBlocking {
        val cacheExpected = 5
        database.withTransaction {
            val list = mapper.listMap(FakeDataProvider.generateArticle(cacheExpected))
            database.articleKeysDao().insert(list.map { ArticleKeys(it.id, null, 2) })
            database.articleDao().insert(list)
        }
        val mockResponse = MockResponse().withResponse("error.json", 400)
        mockWebServer.enqueue(mockResponse)

        onView(withId(R.id.list_article))
            .check(matches(isDisplayed())) // check if recyclerview is displayed
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(0)) // scroll to first item
            .check(matches(atPosition(0, hasDescendant(withId(R.id.retry_button_item))))) // check if retry button is displayed
            .check(matches(itemOfRecyclerEqualWith(cacheExpected + 1))) // check if recyclerview item is equal cache + error item
    }

    @Test
    fun launchHomeFragment_with_ServerSuccessResponse() {
        val mockResponse = MockResponse().withResponse("success.json", 200)

        mockWebServer.enqueue(mockResponse)

        onView(withId(R.id.list_article))
            .check(matches(isDisplayed()))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(0)) // scroll to first item
            .check(matches(atPosition(0, hasDescendant(withId(R.id.article_image))))) // check if first item has ListArticleAdapter
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
        database.apply {
            clearAllTables()
            close()
        }
    }
}
