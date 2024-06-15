package com.capstonewahwah.wastify.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.capstonewahwah.wastify.data.Repository
import com.capstonewahwah.wastify.data.remote.response.ArticlesResponse
import com.capstonewahwah.wastify.data.remote.response.UserDetailsResponse

class HomeViewModel(private val repository: Repository) : ViewModel() {
    val articles: LiveData<ArticlesResponse> = repository.articles
    val articlesLoading: LiveData<Boolean> = repository.articlesLoading
    val userDetails: LiveData<UserDetailsResponse> = repository.userDetails

    fun getArticles() = repository.getArticles()
    fun getUserDetails(token: String) = repository.getUserDetails(token)

    fun getHistories(token: String) = repository.getHistories(token)
}