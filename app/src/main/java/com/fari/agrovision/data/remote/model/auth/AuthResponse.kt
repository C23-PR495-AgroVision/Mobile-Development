package com.fari.agrovision.data.remote.model.auth

import com.google.gson.annotations.SerializedName

data class AuthResponse(

    @field:SerializedName("data")
    val data: DataAuth? = null,

    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("message")
    val message: String
)