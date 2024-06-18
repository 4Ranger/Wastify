package com.capstonewahwah.wastify.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstonewahwah.wastify.R
import com.capstonewahwah.wastify.data.remote.response.ArticlesItem
import com.capstonewahwah.wastify.databinding.ArticleLayoutBinding
import com.capstonewahwah.wastify.ui.main.home.HomeFragmentDirections

class ArticleAdapter(private val isLoading: Boolean) : ListAdapter<ArticlesItem, ArticleAdapter.ViewHolder>(DIFF_CALLBACK) {
    inner class ViewHolder(private val binding: ArticleLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: ArticlesItem) {
            // check if the image is null, if it is then display a default image
            if (article.urlToImage == null) {
                Glide.with(binding.root.context)
                    .load("https://cdn.discordapp.com/attachments/1231980333399015477/1233585134788153354/pexels-ella-olsson-572949-1640770.jpg?ex=662da12f&is=662c4faf&hm=1bd0e6fbb931ed4cdd1fd01cc5631fbb8142ee8f4b4a8add870b0b7b9c7ca7f9&")
                    .into(binding.ivArticle)
            } else {
                Glide.with(binding.root.context)
                    .load(article.urlToImage)
                    .into(binding.ivArticle)
            }
            binding.tvTitle.text = article.title
            binding.tvSource.text = article.source.name

            binding.root.setOnClickListener {
                val toArticleDetails = HomeFragmentDirections.actionNavigationHomeToArticleDetailsFragment()
                toArticleDetails.url = article.url
                it.findNavController().navigate(toArticleDetails)
            }

            if (isLoading) binding.skeletonLayout.showSkeleton() else binding.skeletonLayout.showOriginal()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ArticleLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = getItem(position)
        if (article.title != "[Removed]") {
            holder.itemView.visibility = View.VISIBLE
            holder.itemView.layoutParams.width = holder.itemView.resources.getDimensionPixelSize(R.dimen.articles_width)
            holder.bind(article)
        } else {
            holder.itemView.visibility = View.GONE
            holder.itemView.layoutParams.width = 0
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ArticlesItem>() {
            override fun areItemsTheSame(
                oldItem: ArticlesItem,
                newItem: ArticlesItem
            ): Boolean {
                return oldItem == newItem
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                oldItem: ArticlesItem,
                newItem: ArticlesItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}