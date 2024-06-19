package com.fouranger.wastify.data.remote.response

import com.google.gson.annotations.SerializedName

data class LeaderboardsResponse(

	@field:SerializedName("leaderboard")
	val leaderboard: List<LeaderboardItem>,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class CreatedAt1(

	@field:SerializedName("_nanoseconds")
	val nanoseconds: Int,

	@field:SerializedName("_seconds")
	val seconds: Int
)

data class LeaderboardItem(

	@field:SerializedName("uid")
	val uid: String,

	@field:SerializedName("createdAt")
	val createdAt: CreatedAt1,

	@field:SerializedName("historyPoints")
	val historyPoints: Int,

	@field:SerializedName("rank")
	val rank: Int,

	@field:SerializedName("historyCount")
	val historyCount: Int,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("username")
	val username: String,

	@field:SerializedName("profilePhotoUrl")
	val profilePhotoUrl: String,

	@field:SerializedName("history")
	val history: List<String>
)
