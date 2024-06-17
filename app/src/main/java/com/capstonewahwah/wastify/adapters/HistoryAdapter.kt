package com.capstonewahwah.wastify.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstonewahwah.wastify.data.remote.response.HistoryResponseItem
import com.capstonewahwah.wastify.databinding.WasteLayoutBinding
import com.capstonewahwah.wastify.ui.main.classifications.ClassificationsFragmentDirections

class HistoryAdapter(private val isLoading: Boolean) : ListAdapter<HistoryResponseItem, HistoryAdapter.ViewHolder>(DIFF_CALLBACK) {
    inner class ViewHolder(private val binding: WasteLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(history: HistoryResponseItem) {
            Glide.with(binding.root)
                .load(history.imageUrl)
                .into(binding.ivWaste)
            binding.tvClassifications.text = history.predictedClass

            binding.root.setOnClickListener {
                val extras = FragmentNavigatorExtras(
                    binding.ivWaste to "imageDetails",
                    binding.tvClassifications to "classificationDetails"
                )

                val toClassificationDetail = ClassificationsFragmentDirections.actionNavigationClassificationToNavigationDetails()
                toClassificationDetail.classification = history.predictedClass
                toClassificationDetail.image = history.imageUrl
                toClassificationDetail.rot = history.recommendations.rot
                toClassificationDetail.reduce = history.recommendations.reduce
                toClassificationDetail.recycle = history.recommendations.recycle
                toClassificationDetail.refuse = history.recommendations.refuse
                toClassificationDetail.repurpose = history.recommendations.repurpose
                toClassificationDetail.reuse = history.recommendations.reuse
                it.findNavController().navigate(toClassificationDetail, extras)
            }

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