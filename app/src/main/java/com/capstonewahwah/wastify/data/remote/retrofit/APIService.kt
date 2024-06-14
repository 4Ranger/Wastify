package com.capstonewahwah.wastify.data.remote.retrofit

import com.capstonewahwah.wastify.BuildConfig
import com.capstonewahwah.wastify.data.remote.response.ArticlesResponse
import com.capstonewahwah.wastify.data.remote.response.LoginResponse
import com.capstonewahwah.wastify.data.remote.response.RegisterResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface APIService {

    // Register
    @FormUrlEncoded
    @POST("auth/register")
    fun register(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("auth/login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    // Articles
    @GET("everything?q=waste%20trash&sortBy=relevancy&pageSize=10&apiKey=${BuildConfig.NEWS_API_KEY}")
    fun getArticles(): Call<ArticlesResponse>
}