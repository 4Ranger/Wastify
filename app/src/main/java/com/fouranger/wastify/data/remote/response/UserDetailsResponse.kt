package com.fouranger.wastify.data.remote.response

import com.google.gson.annotations.SerializedName

data class UserDetailsResponse(

	@field:SerializedName("login")
	val login: Login
)

data class Login(

	@field:SerializedName("uid")
	val uid: String,

	@field:SerializedName("photoURL")
	val photoURL: String,

	@field:SerializedName("createdAt")
	val createdAt: CreatedAt,

	@field:SerializedName("displayName")
	val displayName: String,

	@field:SerializedName("historyPoints")
	val historyPoints: Int,

	@field:SerializedName("history")
	val history: List<String>,

	@field:SerializedName("historyCount")
	val historyCount: Int,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("username")
	val username: String,

	@field:SerializedName("profilePhotoUrl")
	val profilePhotoUrl: String
)

data class CreatedAt(

	@field:SerializedName("_nanoseconds")
	val nanoseconds: Int,

	@field:SerializedName("_seconds")
	val seconds: Int
)
