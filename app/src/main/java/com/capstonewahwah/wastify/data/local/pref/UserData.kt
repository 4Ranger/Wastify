package com.capstonewahwah.wastify.data.local.pref

data class UserData(
    val username: String,
    val email: String,
    val trashScanned: Int,
    val points: Int,
    val photoUrl: String,
    val firstBoot: Boolean
)