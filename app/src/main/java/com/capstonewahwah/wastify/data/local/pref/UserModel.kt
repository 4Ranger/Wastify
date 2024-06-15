package com.capstonewahwah.wastify.data.local.pref

data class UserModel(
    val userId: String,
    val name: String,
    val token: String,
    val email: String,
    val historyAndPoints: Int,
    val isLoggedIn: Boolean = false
)