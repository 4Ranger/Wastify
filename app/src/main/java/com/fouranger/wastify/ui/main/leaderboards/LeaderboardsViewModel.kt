package com.fouranger.wastify.ui.main.leaderboards

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.fouranger.wastify.data.Repository
import com.fouranger.wastify.data.remote.response.LeaderboardsResponse

class LeaderboardsViewModel(private val repository: Repository) : ViewModel() {
    val leaderboards: LiveData<LeaderboardsResponse> = repository.leaderboards
    val isLoading: LiveData<Boolean> = repository.leaderboardsLoading

    fun getLeaderboards() = repository.getLeaderboards()
}