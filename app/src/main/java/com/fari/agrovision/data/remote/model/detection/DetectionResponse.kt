package com.fari.agrovision.data.remote.model.detection

import com.fari.agrovision.data.remote.model.auth.DataUser
import com.google.gson.annotations.SerializedName

data class DetectionResponse(

    @field:SerializedName("class")
    val model: DataUser,

    @field:SerializedName("confidence")
    val confidence: Float,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("time")
    val time: String
)