package com.fari.agrovision.data.remote.api

import com.fari.agrovision.data.remote.model.detection.DetectionResponse
import okhttp3.MultipartBody
import retrofit2.http.*

interface DetectionApiService {

    @Multipart
    @POST("predict")
    suspend fun postDetectionDiseaseRipe(
        @Field("uid") uid: String,
        @Field("model") model: String,
        @Part file: MultipartBody.Part
    ): DetectionResponse
}