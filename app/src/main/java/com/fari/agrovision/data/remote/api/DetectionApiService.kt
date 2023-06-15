package com.fari.agrovision.data.remote.api

import com.fari.agrovision.data.remote.model.detection.DetectionDataList
import com.fari.agrovision.data.remote.model.detection.DetectionResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface DetectionApiService {

    @Multipart
    @POST("predict")
    suspend fun postDetection(
        @Part("uid") uid: RequestBody,
        @Part("model") model: RequestBody,
        @Part file: MultipartBody.Part
    ): DetectionResponse

    @GET("detection-data")
    suspend fun getDetectionDataList(
    ): DetectionDataList
}