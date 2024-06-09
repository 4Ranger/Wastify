package com.capstonewahwah.wastify.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.capstonewahwah.wastify.data.local.pref.UserModel
import com.capstonewahwah.wastify.data.local.pref.UserPreference
import com.capstonewahwah.wastify.data.remote.response.ArticlesResponse
import com.capstonewahwah.wastify.data.remote.retrofit.APIService
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Repository private constructor(private val userPreference: UserPreference, private val apiService: APIService) {
    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    // Articles

    private val _articles = MutableLiveData<ArticlesResponse>()
    val articles: LiveData<ArticlesResponse> get() = _articles
    private val _articlesLoading = MutableLiveData<Boolean>()
    val articlesLoading: LiveData<Boolean> get() = _articlesLoading

    fun getArticles() {
        _articlesLoading.value = true
        val client = apiService.getArticles()
        client.enqueue(object : Callback<ArticlesResponse> {
            override fun onResponse(
                call: Call<ArticlesResponse>,
                response: Response<ArticlesResponse>
            ) {
                _articlesLoading.value = false
                if (response.isSuccessful) {
                    _articles.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ArticlesResponse>, t: Throwable) {
                _articlesLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }

    companion object {
        private const val TAG = "Repository"

        @Volatile
        private var instance: Repository? = null

        fun getInstance(userPreference: UserPreference, apiService: APIService) : Repository = instance ?: synchronized(this) {
            instance ?: synchronized(this) {
                instance ?: Repository(userPreference, apiService)
            }.also { instance = it }
        }
    }
}