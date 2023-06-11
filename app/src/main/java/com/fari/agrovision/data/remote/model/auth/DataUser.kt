package com.fari.agrovision.data.remote.model.auth

import com.google.gson.annotations.SerializedName

data class DataUser(

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("imgUrl")
    val imgUrl:String?=null
)

