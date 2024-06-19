package com.fouranger.wastify.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.fouranger.wastify.data.Repository
import com.fouranger.wastify.data.local.pref.UserModel
import com.fouranger.wastify.data.remote.response.LoginResponse
import com.fouranger.wastify.data.remote.response.UnhandledResponse
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: Repository) : ViewModel() {
    val login: LiveData<LoginResponse> = repository.login
    val authLoading: LiveData<Boolean> = repository.authLoading
    val response: LiveData<UnhandledResponse> = repository.response

    fun login(email: String, password: String) = repository.login(email, password)

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
}