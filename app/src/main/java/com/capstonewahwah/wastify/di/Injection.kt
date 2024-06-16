package com.capstonewahwah.wastify.di

import android.content.Context
import com.capstonewahwah.wastify.data.Repository
import com.capstonewahwah.wastify.data.local.pref.UserDataPreference
import com.capstonewahwah.wastify.data.local.pref.UserPreference
import com.capstonewahwah.wastify.data.local.pref.dataStore
import com.capstonewahwah.wastify.data.local.pref.userDetailsDataStore
import com.capstonewahwah.wastify.data.remote.retrofit.APIConfig

object Injection {
    fun provideRepository(context: Context): Repository {
        val pref = UserPreference.getInstance(context.dataStore)
        val articlesApiService = APIConfig.getArticlesAPIService()
        val apiService = APIConfig.getAPIService()
        val userDataPref = UserDataPreference.getInstance(context.userDetailsDataStore)
        return Repository.getInstance(pref, articlesApiService, apiService, userDataPref)
    }
}