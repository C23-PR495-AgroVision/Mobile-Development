package com.fari.agrovision.data.remote.model.auth

import com.google.gson.annotations.SerializedName
//import com.fari.agrovision.data.remote.model.detailarticle.DataDetailArticle

data class AuthResponse(

    @field:SerializedName("data")
    val data: DataAuth? = null,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("status")
    val status: String
)