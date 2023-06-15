package com.fari.agrovision.data.remote.model.detection

import android.os.Parcelable
import com.fari.agrovision.data.remote.model.auth.DataUser
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class DetectionDataList(
    @SerializedName("plant-disease")
    val plantDisease: Map<String, PlantDisease>,

    @SerializedName("ripeness")
    val ripeness: Map<String, Ripeness>
)