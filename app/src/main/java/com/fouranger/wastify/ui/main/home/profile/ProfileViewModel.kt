package com.fouranger.wastify.ui.main.home.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.fouranger.wastify.data.Repository
import com.fouranger.wastify.data.remote.response.ChangePwdResponse
import com.fouranger.wastify.data.remote.response.EditUserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ProfileViewModel(private val repository: Repository) : ViewModel() {
    val updatedUser: LiveData<EditUserResponse> = repository.updatedUser
    val pwdChange: LiveData<ChangePwdResponse> = repository.pwdChange
    val pwdIsLoading: LiveData<Boolean> = repository.changePwdLoading
    val edtIsLoading: LiveData<Boolean> = repository.editedProfileLoading

    fun changePwd(
        token: String,
        email: String,
        oldPassword: String,
        newPassword: String
    ) = repository.changePwd(token, email, oldPassword, newPassword)

    fun updateUser(
        token: String,
        email: RequestBody?,
        username: RequestBody?,
        file: MultipartBody.Part?
    ) = repository.updateUser(token, email, username, file)
}