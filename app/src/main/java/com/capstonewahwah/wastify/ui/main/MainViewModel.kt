package com.capstonewahwah.wastify.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.capstonewahwah.wastify.data.Repository
import com.capstonewahwah.wastify.data.local.pref.UserData
import com.capstonewahwah.wastify.data.local.pref.UserModel
import com.capstonewahwah.wastify.data.remote.response.UserDetailsResponse
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository) : ViewModel() {
    val userDetails: LiveData<UserDetailsResponse> = repository.userDetails

    fun getArticles() = repository.getArticles()

    // session
    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun getUserDetails(token: String) = repository.getUserDetails(token)

    // User Data
    fun saveUserData(user: UserData) {
        viewModelScope.launch {
            repository.saveUserData(user)
        }
    }

    fun getUserData(): LiveData<UserData> {
        return repository.getUserData().asLiveData()
    }

    fun deleteUserData() {
        viewModelScope.launch {
            repository.deleteUserData()
        }
    }
}