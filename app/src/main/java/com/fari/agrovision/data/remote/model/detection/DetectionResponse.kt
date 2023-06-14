package com.fari.agrovision.data.remote.model.detection

import android.os.Parcelable
import com.fari.agrovision.data.remote.model.auth.DataUser
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DetectionResponse(

    @field:SerializedName("class")
    val model: String,

    @field:SerializedName("confidence")
    val confidence: Float,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("time")
    val time: String
) : Parcelable