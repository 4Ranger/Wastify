package com.fouranger.wastify.data.remote.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("loginResult")
	val loginResult: LoginResult? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class LoginResult(

	@field:SerializedName("uid")
	val uid: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: CreatedAt2? = null,

	@field:SerializedName("historyPoints")
	val historyPoints: Int? = null,

	@field:SerializedName("history")
	val history: List<String?>? = null,

	@field:SerializedName("historyCount")
	val historyCount: Int? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("username")
	val username: String? = null,

	@field:SerializedName("token")
	val token: String? = null
)

data class CreatedAt2(

	@field:SerializedName("_nanoseconds")
	val nanoseconds: Int? = null,

	@field:SerializedName("_seconds")
	val seconds: Int? = null
)
