package com.fari.agrovision.data.remote.model.detection

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Ripeness(

    @field:SerializedName("model")
    val model: String,

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("imgUrl")
    val imgUrl: String
) : Parcelable