package com.fouranger.wastify.data.remote.response

import com.google.gson.annotations.SerializedName

data class ChangePwdResponse(

	@field:SerializedName("message")
	val message: String
)
