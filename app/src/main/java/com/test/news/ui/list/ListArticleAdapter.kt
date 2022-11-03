/*
 * Copyright (C) 2022 Budi Ardianata.
 */
package com.test.news.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.test.news.R
import com.test.news.databinding.ItemArticleBinding
import com.test.news.domain.model.Article

class ListArticleAdapter(
    private val onItemClick: (View, Article) -> Unit,
) : PagingDataAdapter<Article, ListArticleAdapter.ListArticleViewHolder>(DIFF_CALLBACK) {

    inner class ListArticleViewHolder(
        private val binding: ItemArticleBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Article, onItemClick: (View, Article) -> Unit) {
            with(binding) {
                Glide.with(root.context)
                    .load(article.urlToImage)
                    .placeholder(R.drawable.ic_logo)
                    .into(articleImage)
                articleDescription.text = article.description
                articleTitle.text = article.title
                articlePublishedAt.text = article.publishedAtFormatted
                articleSource.text = article.source.name
                ViewCompat.setTransitionName(
                    articleRoot,
                    "article_card_${article.id}"
                )
                articleRoot.setOnClickListener {
                    onItemClick(it, article)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: ListArticleViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it, onItemClick)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListArticleViewHolder {
        return ListArticleViewHolder(
            ItemArticleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}
