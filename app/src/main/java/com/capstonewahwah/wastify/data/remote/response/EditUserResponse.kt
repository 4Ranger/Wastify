package com.capstonewahwah.wastify.data.remote.response

import com.google.gson.annotations.SerializedName

data class EditUserResponse(

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("url")
	val url: String? = null
)
