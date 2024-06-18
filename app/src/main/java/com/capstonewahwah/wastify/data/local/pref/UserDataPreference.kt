package com.capstonewahwah.wastify.data.local.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
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
            preferences[USERNAME_KEY] = user.username
            preferences[EMAIL_KEY] = user.email
            preferences[TRASHSCANNED_KEY] = user.trashScanned
            preferences[POINTS_KEY] = user.points
            preferences[PHOTO_KEY] = user.photoUrl
            preferences[FIRSTBOOT_KEY] = user.firstBoot
        }
    }

    fun getUserData(): Flow<UserData> {
        return dataStore.data.map { preferences ->
            UserData(
                preferences[USERNAME_KEY] ?: "",
                preferences[EMAIL_KEY] ?: "",
                preferences[TRASHSCANNED_KEY] ?: 0,
                preferences[POINTS_KEY] ?: 0,
                preferences[PHOTO_KEY] ?: "",
                preferences[FIRSTBOOT_KEY] ?: true
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

        private val USERNAME_KEY = stringPreferencesKey("username")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val TRASHSCANNED_KEY = intPreferencesKey("trashScanned")
        private val POINTS_KEY = intPreferencesKey("points")
        private val PHOTO_KEY = stringPreferencesKey("photoUrl")
        private val FIRSTBOOT_KEY = booleanPreferencesKey("firstBoot")

        fun getInstance(userDetailDataStore: DataStore<Preferences>): UserDataPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserDataPreference(userDetailDataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}