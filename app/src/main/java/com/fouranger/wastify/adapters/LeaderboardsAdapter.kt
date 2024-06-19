package com.fouranger.wastify.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fouranger.wastify.R
import com.fouranger.wastify.data.remote.response.LeaderboardItem
import com.fouranger.wastify.databinding.LeaderboardLayoutBinding

class LeaderboardsAdapter(private val isLoading: Boolean) : ListAdapter<LeaderboardItem, LeaderboardsAdapter.ViewModel>(DIFF_CALLBACK) {
    inner class ViewModel(private val binding: LeaderboardLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(leaderboard: LeaderboardItem) {
            binding.tvUsername.text = leaderboard.username
            binding.tvPoints.text = binding.root.context.getString(R.string.user_point, leaderboard.historyPoints)

            when (leaderboard.rank) {
                1 -> {
                    binding.ivRank1.visibility = View.VISIBLE
                    binding.tvRank.visibility = View.GONE
                }
                2 -> {
                    binding.ivRank2.visibility = View.VISIBLE
                    binding.tvRank.visibility = View.GONE
                }
                3 -> {
                    binding.ivRank3.visibility = View.VISIBLE
                    binding.tvRank.visibility = View.GONE
                }
                else -> {
                    binding.tvRank.visibility = View.VISIBLE
                    binding.tvRank.text = binding.root.context.getString(R.string.rank, leaderboard.rank)
                    binding.ivRank1.visibility = View.GONE
                    binding.ivRank2.visibility = View.GONE
                    binding.ivRank3.visibility = View.GONE
                }
            }
            if (isLoading) binding.skeletonLayout.showSkeleton() else binding.skeletonLayout.showOriginal()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewModel {
        val binding = LeaderboardLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewModel(binding)
    }

    override fun onBindViewHolder(holder: ViewModel, position: Int) {
        val leaderboard = getItem(position)
        holder.bind(leaderboard)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<LeaderboardItem>() {
            override fun areItemsTheSame(
                oldItem: LeaderboardItem,
                newItem: LeaderboardItem
            ): Boolean {
                return oldItem == newItem
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                oldItem: LeaderboardItem,
                newItem: LeaderboardItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}