package com.capstonewahwah.wastify.ui.main.home.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.capstonewahwah.wastify.data.Repository
import com.capstonewahwah.wastify.data.remote.response.EditProfileResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ProfileViewModel(private val repository: Repository) : ViewModel() {
    val editedProfile: LiveData<EditProfileResponse> = repository.editedProfile

    fun editProfile(
        token: String,
        username: RequestBody,
        email: RequestBody,
        file: MultipartBody.Part
    ) = repository.updateProfile(token, username, email, file)
}