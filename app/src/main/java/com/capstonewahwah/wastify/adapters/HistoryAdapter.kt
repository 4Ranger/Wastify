package com.capstonewahwah.wastify.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstonewahwah.wastify.data.remote.response.HistoryResponseItem
import com.capstonewahwah.wastify.databinding.WasteLayoutBinding

class HistoryAdapter(private val isLoading: Boolean) : ListAdapter<HistoryResponseItem, HistoryAdapter.ViewHolder>(DIFF_CALLBACK) {
    inner class ViewHolder(private val binding: WasteLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(history: HistoryResponseItem) {
            Glide.with(binding.root)
                .load(history.imageUrl)
                .into(binding.ivWaste)
            binding.tvClassifications.text = history.predictedClass
//            binding.tvDetails.text = history.recommendations.rot
            if (isLoading) binding.skeletonLayout.showSkeleton() else binding.skeletonLayout.showOriginal()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = WasteLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val history = getItem(position)
        holder.bind(history)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HistoryResponseItem>() {
            override fun areItemsTheSame(
                oldItem: HistoryResponseItem,
                newItem: HistoryResponseItem
            ): Boolean {
                return oldItem == newItem
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                oldItem: HistoryResponseItem,
                newItem: HistoryResponseItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}