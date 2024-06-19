package com.fouranger.wastify.data.remote.response

import com.google.gson.annotations.SerializedName

data class PredictResponse(

	@field:SerializedName("imageUrl")
	val imageUrl: String,

	@field:SerializedName("predictedClass")
	val predictedClass: String,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("recommendations")
	val recommendations: Recommendations
)

data class Recommendations(

	@field:SerializedName("reduce")
	val reduce: String,

	@field:SerializedName("reuse")
	val reuse: String,

	@field:SerializedName("refuse")
	val refuse: String,

	@field:SerializedName("rot")
	val rot: String,

	@field:SerializedName("repurpose")
	val repurpose: String,

	@field:SerializedName("recycle")
	val recycle: String
)
