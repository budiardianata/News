/*
 * Copyright (C) 2022 Budi Ardianata.
 */
package com.test.news.ui.detail

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.google.android.material.transition.MaterialContainerTransform
import com.test.news.R
import com.test.news.databinding.FragmentDetailBinding
import com.test.news.domain.model.Article
import com.test.news.util.Constant
import com.test.news.util.parcelable
import com.test.news.util.themeColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment(R.layout.fragment_detail) {

    private val binding by viewBinding(FragmentDetailBinding::bind)
    private val navController by lazy { findNavController() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment
            duration = 300L
            scrimColor = Color.TRANSPARENT
            setAllContainerColors(requireContext().themeColor(com.google.android.material.R.attr.colorSurface))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val article = requireArguments().parcelable<Article>(Constant.KEY_ARTICLE)
        article?.let(::setupContent)
        binding.collapsingToolbar.setupWithNavController(
            binding.toolBarLayout,
            navController
        )
    }

    private fun setupContent(article: Article) {
        binding.run {
            Glide.with(requireContext())
                .load(article.urlToImage)
                .into(imageView)
            detailTitle.text = article.title
            detailContent.text = article.content ?: article.description
            publishedAt.text = article.publishedAtFormatted
            detailSource.text = article.source.name
            collapsingToolbar.title = article.title
            readMore.text = requireContext().getString(R.string.read_more, article.source.name)
            readMore.setOnClickListener {
                Intent(Intent.ACTION_VIEW, article.url.toUri()).also(::startActivity)
            }
        }
    }
}
