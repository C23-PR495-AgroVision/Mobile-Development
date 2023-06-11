package com.fari.agrovision.data.remote.model.auth

import com.google.gson.annotations.SerializedName

data class DataAuth(

	@field:SerializedName("uid")
	val uid: String,

	@field:SerializedName("email")
	val email: String
)
