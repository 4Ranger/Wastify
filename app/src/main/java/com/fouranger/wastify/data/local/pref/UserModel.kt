package com.fouranger.wastify.data.local.pref

data class UserModel(
    val userId: String,
    val username: String,
    val token: String,
    val isLoggedIn: Boolean = false
)