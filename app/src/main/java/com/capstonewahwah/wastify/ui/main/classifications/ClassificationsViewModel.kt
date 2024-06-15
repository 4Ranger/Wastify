package com.capstonewahwah.wastify.ui.main.classifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.capstonewahwah.wastify.data.Repository
import com.capstonewahwah.wastify.data.remote.response.HistoryResponse
import com.capstonewahwah.wastify.data.remote.response.HistoryResponseItem

class ClassificationsViewModel(private val repository: Repository) : ViewModel() {
    val histories: LiveData<List<HistoryResponseItem>> = repository.history
    val isLoading: LiveData<Boolean> = repository.historyLoading

    fun getHistories(token: String) = repository.getHistories(token)
}