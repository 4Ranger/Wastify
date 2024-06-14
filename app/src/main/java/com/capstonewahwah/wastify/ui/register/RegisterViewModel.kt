package com.capstonewahwah.wastify.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.capstonewahwah.wastify.data.Repository
import com.capstonewahwah.wastify.data.remote.response.RegisterResponse

class RegisterViewModel(private val repository: Repository) : ViewModel() {

    val register: LiveData<RegisterResponse> = repository.register
    val authLoading: LiveData<Boolean> = repository.authLoading

    fun register(
        username: String,
        email: String,
        password: String
    ) = repository.register(username, email, password)
}