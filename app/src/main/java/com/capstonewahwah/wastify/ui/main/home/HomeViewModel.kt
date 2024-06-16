package com.capstonewahwah.wastify.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.capstonewahwah.wastify.data.Repository
import com.capstonewahwah.wastify.data.remote.response.ArticlesResponse

class HomeViewModel(private val repository: Repository) : ViewModel() {
    val articles: LiveData<ArticlesResponse> = repository.articles
    val articlesLoading: LiveData<Boolean> = repository.articlesLoading
}