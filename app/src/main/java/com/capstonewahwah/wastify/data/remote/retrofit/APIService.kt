package com.capstonewahwah.wastify.data.remote.retrofit

import com.capstonewahwah.wastify.BuildConfig
import com.capstonewahwah.wastify.data.remote.response.ArticlesResponse
import com.capstonewahwah.wastify.data.remote.response.ChangePwdResponse
import com.capstonewahwah.wastify.data.remote.response.EditUserResponse
import com.capstonewahwah.wastify.data.remote.response.HistoryResponseItem
import com.capstonewahwah.wastify.data.remote.response.LeaderboardsResponse
import com.capstonewahwah.wastify.data.remote.response.LoginResponse
import com.capstonewahwah.wastify.data.remote.response.PredictResponse
import com.capstonewahwah.wastify.data.remote.response.RegisterResponse
import com.capstonewahwah.wastify.data.remote.response.UserDetailsResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part

interface APIService {

    // Register
    @FormUrlEncoded
    @POST("auth/register")
    fun register(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    // Login
    @FormUrlEncoded
    @POST("auth/login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    // Predict
    @Multipart
    @POST("predict/image")
    fun predict(
        @Header("Authorization") token: String,
        @Part image: MultipartBody.Part
    ): Call<PredictResponse>

    // Prediction Histories
    @GET("predict/history")
    fun getHistories(
        @Header("Authorization") token: String
    ): Call<List<HistoryResponseItem>>

    // User Details
    @GET("auth/profile")
    fun getUserDetails(
        @Header("Authorization") token: String
    ): Call<UserDetailsResponse>

    // Edit User
    @Multipart
    @PATCH("auth/editProfile")
    fun updateUser(
        @Header("Authorization") token: String,
        @Part("email") email: RequestBody?,
        @Part("username") username: RequestBody?,
        @Part file: MultipartBody.Part?
    ): Call<EditUserResponse>

    // Change Password
    @FormUrlEncoded
    @POST("auth/changePassword")
    fun changePwd(
        @Header("Authorization") token: String,
        @Field("email") email: String,
        @Field("oldPassword") oldPassword: String,
        @Field("newPassword") newPassword: String
    ): Call<ChangePwdResponse>

    // Leaderboards
    @GET("auth/leaderboard")
    fun getLeaderboards(): Call<LeaderboardsResponse>

    // Articles
    @GET("everything?q=waste%20trash&sortBy=relevancy&pageSize=10&apiKey=${BuildConfig.NEWS_API_KEY}")
    fun getArticles(): Call<ArticlesResponse>
}