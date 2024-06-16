package com.capstonewahwah.wastify.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.capstonewahwah.wastify.data.local.pref.UserModel
import com.capstonewahwah.wastify.data.local.pref.UserPreference
import com.capstonewahwah.wastify.data.remote.response.ArticlesResponse
import com.capstonewahwah.wastify.data.remote.response.EditProfileResponse
import com.capstonewahwah.wastify.data.remote.response.HistoryResponse
import com.capstonewahwah.wastify.data.remote.response.HistoryResponseItem
import com.capstonewahwah.wastify.data.remote.response.LeaderboardsResponse
import com.capstonewahwah.wastify.data.remote.response.LoginResponse
import com.capstonewahwah.wastify.data.remote.response.PredictResponse
import com.capstonewahwah.wastify.data.remote.response.RegisterResponse
import com.capstonewahwah.wastify.data.remote.response.UserDetailsResponse
import com.capstonewahwah.wastify.data.remote.retrofit.APIService
import com.capstonewahwah.wastify.helper.SingleLiveEvent
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    // Predict
    private val _predict = SingleLiveEvent<PredictResponse>()
    val predict: LiveData<PredictResponse> get() = _predict

    private val _predictLoading = MutableLiveData<Boolean>()
    val predictLoading: LiveData<Boolean> get() = _predictLoading

    fun predict(
        token: String,
        image: MultipartBody.Part
    ) {
        _predictLoading.value = true
        val client = apiService.predict(
            token,
            image
        )
        client.enqueue(object : Callback<PredictResponse> {
            override fun onResponse(
                call: Call<PredictResponse>,
                response: Response<PredictResponse>
            ) {
                _predictLoading.value = false
                if (response.isSuccessful) {
                    _predict.value = response.body()
                } else {
                    Log.e(TAG, "onFailure1: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<PredictResponse>, t: Throwable) {
                _predictLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    // Get Prediction History
    private val _history = MutableLiveData<List<HistoryResponseItem>>()
    val history: LiveData<List<HistoryResponseItem>> get() = _history

    private val _historyLoading = MutableLiveData<Boolean>()
    val historyLoading: LiveData<Boolean> get() = _historyLoading

    fun getHistories(token: String) {
        _historyLoading.value = true
        val client = apiService.getHistories(token)
        client.enqueue(object : Callback<List<HistoryResponseItem>> {
            override fun onResponse(
                call: Call<List<HistoryResponseItem>>,
                response: Response<List<HistoryResponseItem>>
            ) {
                _historyLoading.value = false
                if (response.isSuccessful) {
                    _history.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<HistoryResponseItem>>, t: Throwable) {
                _historyLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    // User Details
    private val _userDetails = MutableLiveData<UserDetailsResponse>()
    val userDetails: LiveData<UserDetailsResponse> get() = _userDetails
    fun getUserDetails(token: String) {
        val client = apiService.getUserDetails(token)
        client.enqueue(object : Callback<UserDetailsResponse> {
            override fun onResponse(
                call: Call<UserDetailsResponse>,
                response: Response<UserDetailsResponse>
            ) {
                if (response.isSuccessful) {
                    _userDetails.value = response.body()
                } else {
                    Log.e(TAG, "onFailure1: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserDetailsResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    // Edit Profile
    private val _editedProfile = MutableLiveData<EditProfileResponse>()
    val editedProfile: LiveData<EditProfileResponse> get() = _editedProfile

    fun updateProfile(token: String, username: RequestBody, email: RequestBody, file: MultipartBody.Part) {
        val client = apiService.editProfile(token, username, email, file)
        client.enqueue(object : Callback<EditProfileResponse> {
            override fun onResponse(
                call: Call<EditProfileResponse>,
                response: Response<EditProfileResponse>
            ) {
                if (response.isSuccessful) {
                    _editedProfile.value = response.body()
                } else {
                    Log.e(TAG, "onFailure1: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EditProfileResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    // Leaderboards
    private val _leaderboards = MutableLiveData<LeaderboardsResponse>()
    val leaderboards: LiveData<LeaderboardsResponse> get() = _leaderboards

    private val _leaderboardsLoading = MutableLiveData<Boolean>()
    val leaderboardsLoading: LiveData<Boolean> get() = _leaderboardsLoading

    fun getLeaderboards() {
        _leaderboardsLoading.value = true
        val client = apiService.getLeaderboards()
        client.enqueue(object : Callback<LeaderboardsResponse> {
            override fun onResponse(
                call: Call<LeaderboardsResponse>,
                response: Response<LeaderboardsResponse>
            ) {
                _leaderboardsLoading.value = false
                if (response.isSuccessful) {
                    _leaderboards.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LeaderboardsResponse>, t: Throwable) {
                _leaderboardsLoading.value = false
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