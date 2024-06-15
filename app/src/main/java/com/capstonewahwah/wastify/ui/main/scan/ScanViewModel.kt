package com.capstonewahwah.wastify.ui.main.scan

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.capstonewahwah.wastify.data.Repository
import com.capstonewahwah.wastify.data.remote.response.PredictResponse
import okhttp3.MultipartBody

class ScanViewModel(private val repository: Repository) : ViewModel() {
    val predict: LiveData<PredictResponse> = repository.predict
    val isLoading: LiveData<Boolean> = repository.predictLoading

    fun predict(
        token: String,
        image: MultipartBody.Part
    ) = repository.predict(
        token,
        image
    )
}