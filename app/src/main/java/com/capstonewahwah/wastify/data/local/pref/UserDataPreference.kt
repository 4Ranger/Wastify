package com.capstonewahwah.wastify.data.local.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.userDetailsDataStore: DataStore<Preferences> by preferencesDataStore(name = "userData")
class UserDataPreference private constructor(private val dataStore: DataStore<Preferences>) {
    suspend fun saveUserData(user: UserData) {
        dataStore.edit { preferences ->
            preferences[EMAIL_KEY] = user.email
            preferences[TRASHSCANNED_KEY] = user.trashScanned
            preferences[POINTS_KEY] = user.points
            preferences[PHOTO_KEY] = user.photoUrl
        }
    }

    fun getUserData(): Flow<UserData> {
        return dataStore.data.map { preferences ->
            UserData(
                preferences[EMAIL_KEY] ?: "",
                preferences[TRASHSCANNED_KEY] ?: 0,
                preferences[POINTS_KEY] ?: 0,
                preferences[PHOTO_KEY] ?: ""
            )
        }
    }

    suspend fun deleteUserData() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserDataPreference? = null

        private val EMAIL_KEY = stringPreferencesKey("email")
        private val TRASHSCANNED_KEY = intPreferencesKey("trashScanned")
        private val POINTS_KEY = intPreferencesKey("points")
        private val PHOTO_KEY = stringPreferencesKey("photoUrl")

        fun getInstance(userDetailDataStore: DataStore<Preferences>): UserDataPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserDataPreference(userDetailDataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}