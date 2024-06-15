package com.capstonewahwah.wastify.data.remote.response

import com.google.gson.annotations.SerializedName

data class HistoryResponse(

	@field:SerializedName("HistoryResponse")
	val historyResponse: List<HistoryResponseItem>
)

data class Prediction(

	@field:SerializedName("class_0")
	val class0: Any,

	@field:SerializedName("class_2")
	val class2: Any,

	@field:SerializedName("class_1")
	val class1: Any,

	@field:SerializedName("class_4")
	val class4: Any,

	@field:SerializedName("class_3")
	val class3: Any,

	@field:SerializedName("class_5")
	val class5: Any
)

data class Recommendations1(

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

data class HistoryResponseItem(

	@field:SerializedName("imageUrl")
	val imageUrl: String,

	@field:SerializedName("predictedClass")
	val predictedClass: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("recommendations")
	val recommendations: Recommendations1,

	@field:SerializedName("timestamp")
	val timestamp: Timestamp,

	@field:SerializedName("prediction")
	val prediction: Prediction
)

data class Timestamp(

	@field:SerializedName("_nanoseconds")
	val nanoseconds: Int,

	@field:SerializedName("_seconds")
	val seconds: Int
)
