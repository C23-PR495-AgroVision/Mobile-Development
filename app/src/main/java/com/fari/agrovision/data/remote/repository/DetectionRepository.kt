package com.fari.agrovision.data.remote.repository

import android.util.Log
import com.fari.agrovision.data.remote.api.DetectionApiService
import com.fari.agrovision.data.remote.model.detection.DetectionDataList
import com.fari.agrovision.data.local.Result
import com.fari.agrovision.data.remote.model.detection.DetectionResponse
import com.fari.agrovision.data.remote.model.detection.PlantDisease
import com.fari.agrovision.data.remote.model.detection.Ripeness
import com.fari.agrovision.data.remote.utils.reduceFileImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class DetectionRepository private constructor(
    private val detectionApiService: DetectionApiService
) {

    fun getListDetectionDisease(): Flow<Result<List<PlantDisease>>> = flow {
        emit(Result.Loading)
        try {
            val response = detectionApiService.getDetectionDataList()
            val plantDiseaseList = response.plantDisease.values.toList()
            emit(Result.Success(plantDiseaseList))
        } catch (e: Exception) {
            Log.d("DetectionRepository", "getListDisease: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getListDetectionRipeness(): Flow<Result<List<Ripeness>>> = flow {
        emit(Result.Loading)
        try {
            val response = detectionApiService.getDetectionDataList()
            val ripenessList = response.ripeness.values.toList()
            emit(Result.Success(ripenessList))
        } catch (e: Exception) {
            Log.d("DetectionRepository", "getListRipeness: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun postDetection(
        uid: String, model: String, imageFile: File
    ): Flow<Result<DetectionResponse>> = flow {
        emit(Result.Loading)
        try{
            val reducedFile = reduceFileImage(imageFile)
            val requestImageFile = reducedFile.asRequestBody("image/jpeg".toMediaType())
            val imageMultipart: MultipartBody.Part =
                MultipartBody.Part.createFormData("file", imageFile.name, requestImageFile)
            val uidRequestBody = uid.toRequestBody("text/plain".toMediaType())
            val modelRequestBody = model.toRequestBody("text/plain".toMediaType())
            val response = detectionApiService.postDetection(
                uidRequestBody, modelRequestBody, imageMultipart
            )
            emit(Result.Success(response))
        }catch (e:Exception){
            Log.d("DetectionRepository", "postDetection: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    companion object {
        @Volatile
        private var instance: DetectionRepository? = null
        fun getInstance(
            apiService: DetectionApiService
        ): DetectionRepository = instance ?: synchronized(this) {
            instance ?: DetectionRepository(apiService)
        }.also { instance = it }
    }
}