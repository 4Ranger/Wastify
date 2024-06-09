package com.capstonewahwah.wastify.data.remote.retrofit

import com.capstonewahwah.wastify.BuildConfig
import com.capstonewahwah.wastify.data.remote.response.ArticlesResponse
import retrofit2.Call
import retrofit2.http.GET

interface APIService {
    // Articles
    @GET("everything?q=waste%20trash&sortBy=relevancy&pageSize=10&apiKey=${BuildConfig.NEWS_API_KEY}")
    fun getArticles(): Call<ArticlesResponse>
}