/*
 * Copyright (C) 2022 Budi Ardianata.
 */
package com.test.news.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.test.news.domain.model.Article
import com.test.news.domain.repositories.ArticleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update

@HiltViewModel
class ListViewModel @Inject constructor(
    private val repository: ArticleRepository,
) : ViewModel() {

    val query = MutableStateFlow<String?>(null)

    @ExperimentalCoroutinesApi
    val articlePagedData: Flow<PagingData<Article>> = query.flatMapLatest { query ->
        repository.getPagedArticle(query)
    }.cachedIn(viewModelScope)

    fun onQueryChanged(query: String?) {
        this.query.update { query }
    }
}
