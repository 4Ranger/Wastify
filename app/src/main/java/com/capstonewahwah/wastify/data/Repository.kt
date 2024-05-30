package com.capstonewahwah.wastify.data

import com.capstonewahwah.wastify.data.local.pref.UserModel
import com.capstonewahwah.wastify.data.local.pref.UserPreference
import kotlinx.coroutines.flow.Flow

class Repository private constructor(private val userPreference: UserPreference) {
    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        private const val TAG = "Repository"

        @Volatile
        private var instance: Repository? = null

        fun getInstance(userPreference: UserPreference) : Repository = instance ?: synchronized(this) {
            instance ?: synchronized(this) {
                instance ?: Repository(userPreference)
            }.also { instance = it }
        }
    }
}