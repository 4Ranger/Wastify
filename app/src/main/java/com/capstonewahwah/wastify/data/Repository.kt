package com.capstonewahwah.wastify.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.capstonewahwah.wastify.data.local.pref.UserModel
import com.capstonewahwah.wastify.data.local.pref.UserPreference
import com.capstonewahwah.wastify.data.remote.response.ArticlesResponse
import com.capstonewahwah.wastify.data.remote.response.LoginResponse
import com.capstonewahwah.wastify.data.remote.response.RegisterResponse
import com.capstonewahwah.wastify.data.remote.retrofit.APIService
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Repository private constructor(
    private val userPreference: UserPreference,
    private val articlesApiService: APIService,
    private val apiService: APIService
) {
    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    // Auth
    private val _authLoading = MutableLiveData<Boolean>()
    val authLoading: LiveData<Boolean> get() =  _authLoading

    // Register
    private val _register = MutableLiveData<RegisterResponse>()
    val register: LiveData<RegisterResponse> get() = _register

    fun register(username: String, email: String, password: String) {
        _authLoading.value = true
        val client = apiService.register(username, email, password)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                _authLoading.value = false
                if (response.isSuccessful) {
                    _register.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _authLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    // Login
    private val _login = MutableLiveData<LoginResponse>()
    val login: LiveData<LoginResponse> get() = _login

    fun login(email: String, password: String) {
        _authLoading.value = true
        val client = apiService.login(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                _authLoading.value = false
                if (response.isSuccessful) {
                    _login.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _authLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    // Articles

    private val _articles = MutableLiveData<ArticlesResponse>()
    val articles: LiveData<ArticlesResponse> get() = _articles
    private val _articlesLoading = MutableLiveData<Boolean>()
    val articlesLoading: LiveData<Boolean> get() = _articlesLoading

    fun getArticles() {
        _articlesLoading.value = true
        val client = articlesApiService.getArticles()
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

        fun getInstance(userPreference: UserPreference, articlesApiService: APIService, apiService: APIService) : Repository = instance ?: synchronized(this) {
            instance ?: synchronized(this) {
                instance ?: Repository(userPreference, articlesApiService, apiService)
            }.also { instance = it }
        }
    }
}