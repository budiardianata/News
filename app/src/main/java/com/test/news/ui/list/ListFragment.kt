/*
 * Copyright (C) 2022 Budi Ardianata.
 */
package com.test.news.ui.list

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.transition.MaterialElevationScale
import com.test.news.R
import com.test.news.databinding.FragmentListBinding
import com.test.news.domain.model.Article
import com.test.news.util.Constant
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListFragment : Fragment(R.layout.fragment_list), SearchView.OnQueryTextListener {
    private val binding by viewBinding(FragmentListBinding::bind)
    private val viewModel by viewModels<ListViewModel>()
    private val adapter by lazy { ListArticleAdapter(::onArticleClicked) }
    private val navController by lazy { findNavController() }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
        val headerLoadStateAdapter = ListLoadStateAdapter(adapter::retry)
        binding.run {
            setupList(headerLoadStateAdapter)
            setupSearchView()
            swipeRefreshLayout.setOnRefreshListener(adapter::refresh)
        }
        observeData(headerLoadStateAdapter)
    }

    private fun FragmentListBinding.setupSearchView() {
        searchView.run {
            searchView.isSubmitButtonEnabled = true
            searchView.setOnQueryTextListener(this@ListFragment)
        }
    }

    private fun FragmentListBinding.setupList(headerLoadStateAdapter: ListLoadStateAdapter) {
        listArticle.layoutManager = LinearLayoutManager(requireContext())
        listArticle.adapter = adapter.withLoadStateHeaderAndFooter(
            header = headerLoadStateAdapter,
            footer = ListLoadStateAdapter(adapter::retry)
        )
        listArticle.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
    }

    @ExperimentalCoroutinesApi
    private fun observeData(headerStateAdapter: ListLoadStateAdapter) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                launch {
                    adapter.loadStateFlow.collect { loadState ->
                        headerStateAdapter.loadState = getHeaderState(loadState, adapter.itemCount)
                        val isRefreshing = loadState.mediator?.refresh is LoadState.Loading
                        binding.swipeRefreshLayout.isRefreshing = isRefreshing
                    }
                }
                launch {
                    viewModel.articlePagedData.collect(adapter::submitData)
                }
                launch {
                    viewModel.query.collectLatest {
                        if (adapter.itemCount != 0) {
                            binding.listArticle.smoothScrollToPosition(0)
                        }
                    }
                }
            }
        }
    }

    private fun getHeaderState(state: CombinedLoadStates, count: Int): LoadState {
        if (state.refresh is LoadState.NotLoading && count == 0) {
            val emptyMessage = getString(R.string.empty_data)
            return LoadState.Error(NoSuchElementException(emptyMessage))
        }
        val mediatorError = state.mediator?.refresh?.takeIf { it is LoadState.Error && count > 0 }
        val refreshError = state.refresh.takeIf { it is LoadState.Error }

        return mediatorError ?: refreshError ?: state.prepend
    }

    private fun onArticleClicked(view: View, article: Article) {
        exitTransition = MaterialElevationScale(false).apply {
            duration = 300L
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = 300L
        }
        navController.navigate(
            resId = R.id.action_list_to_detail,
            args = bundleOf(Constant.KEY_ARTICLE to article),
            navOptions = null,
            navigatorExtras = FragmentNavigatorExtras(view to "card_detail")
        )
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        viewModel.onQueryChanged(query?.trim())
        binding.searchView.clearFocus()
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText.isNullOrEmpty()) {
            viewModel.onQueryChanged(null)
        }
        return true
    }
}
