package com.fari.agrovision.data.remote.model.auth

import com.google.gson.annotations.SerializedName

data class UserResponse(

	@field:SerializedName("data")
	val data: DataUser? = null,

	@field:SerializedName("status")
	val status: String,

	@field:SerializedName("message")
	val message: String
)

