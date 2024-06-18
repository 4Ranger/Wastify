package com.capstonewahwah.wastify.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.capstonewahwah.wastify.data.local.pref.UserData
import com.capstonewahwah.wastify.data.local.pref.UserDataPreference
import com.capstonewahwah.wastify.data.local.pref.UserModel
import com.capstonewahwah.wastify.data.local.pref.UserPreference
import com.capstonewahwah.wastify.data.remote.response.ArticlesResponse
import com.capstonewahwah.wastify.data.remote.response.ChangePwdResponse
import com.capstonewahwah.wastify.data.remote.response.EditUserResponse
import com.capstonewahwah.wastify.data.remote.response.HistoryResponseItem
import com.capstonewahwah.wastify.data.remote.response.LeaderboardsResponse
import com.capstonewahwah.wastify.data.remote.response.LoginResponse
import com.capstonewahwah.wastify.data.remote.response.PredictResponse
import com.capstonewahwah.wastify.data.remote.response.RegisterResponse
import com.capstonewahwah.wastify.data.remote.response.UnhandledResponse
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
    private val apiService: APIService,
    private val userDataPreference: UserDataPreference
) {

    // Session
    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    // User Data
    suspend fun saveUserData(user: UserData) {
        userDataPreference.saveUserData(user)
    }

    fun getUserData(): Flow<UserData> {
        return userDataPreference.getUserData()
    }

    suspend fun deleteUserData() {
        userDataPreference.deleteUserData()
    }

    private val _response = MutableLiveData<UnhandledResponse>()
    val response: LiveData<UnhandledResponse> = _response

    // Auth
    private val _authLoading = MutableLiveData<Boolean>()
    val authLoading: LiveData<Boolean> get() =  _authLoading

    // Register
    private val _register = SingleLiveEvent<RegisterResponse>()
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
    private val _login = SingleLiveEvent<LoginResponse>()
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
                    if (response.code() == 400) {
                        _response.value = UnhandledResponse(400, "Email atau Password yang anda masukkan salah")
                    }
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
    private val _userDetails = SingleLiveEvent<UserDetailsResponse>()
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
    private val _editedProfileLoading = MutableLiveData<Boolean>()
    val editedProfileLoading: LiveData<Boolean> get() = _editedProfileLoading

    private val _updatedUser = SingleLiveEvent<EditUserResponse>()
    val updatedUser: LiveData<EditUserResponse> get() = _updatedUser

    fun updateUser(token: String, email: RequestBody?, username: RequestBody?, file: MultipartBody.Part?) {
        _editedProfileLoading.value = true
        val client = apiService.updateUser("Bearer $token", email, username, file)
        client.enqueue(object : Callback<EditUserResponse> {
            override fun onResponse(
                call: Call<EditUserResponse>,
                response: Response<EditUserResponse>
            ) {
                _editedProfileLoading.value = false
                if (response.isSuccessful) {
                    _updatedUser.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EditUserResponse>, t: Throwable) {
                _editedProfileLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    // Change Password
    private val _pwdChange = SingleLiveEvent<ChangePwdResponse>()
    val pwdChange: LiveData<ChangePwdResponse> get() = _pwdChange
    private val _changePwdLoading = MutableLiveData<Boolean>()
    val changePwdLoading: LiveData<Boolean> get() = _changePwdLoading

    fun changePwd(token: String, email: String, oldPassword: String, newPassword: String) {
        _changePwdLoading.value = true
        val client = apiService.changePwd("Bearer $token", email, oldPassword, newPassword)
        client.enqueue(object : Callback<ChangePwdResponse> {
            override fun onResponse(
                call: Call<ChangePwdResponse>,
                response: Response<ChangePwdResponse>
            ) {
                _changePwdLoading.value = false
                if (response.isSuccessful) {
                    _pwdChange.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ChangePwdResponse>, t: Throwable) {
                _changePwdLoading.value = false
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

        fun getInstance(
            userPreference: UserPreference,
            articlesApiService: APIService,
            apiService: APIService,
            userDataPreference: UserDataPreference
        ) : Repository = instance ?: synchronized(this) {
            instance ?: synchronized(this) {
                instance ?: Repository(userPreference, articlesApiService, apiService, userDataPreference)
            }.also { instance = it }
        }
    }
}