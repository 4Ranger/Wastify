package com.capstonewahwah.wastify.data.remote.response

import com.google.gson.annotations.SerializedName

data class UserDetailsResponse(

	@field:SerializedName("uid")
	val uid: String,

	@field:SerializedName("createdAt")
	val createdAt: CreatedAt,

	@field:SerializedName("displayName")
	val displayName: String,

	@field:SerializedName("history")
	val history: List<String>,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("username")
	val username: String
)

data class CreatedAt(

	@field:SerializedName("_nanoseconds")
	val nanoseconds: Int,

	@field:SerializedName("_seconds")
	val seconds: Int
)
