package com.fari.agrovision.data.remote.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.util.Base64
import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.fari.agrovision.data.local.Result
import com.fari.agrovision.data.local.preference.UserPreference
import com.fari.agrovision.data.remote.api.UserApiService
import com.fari.agrovision.data.remote.model.auth.DataUser
import com.fari.agrovision.data.remote.utils.reduceFileImage
import kotlinx.coroutines.flow.emitAll
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException

class UserRepository private constructor(
    private val userApiService: UserApiService,
    private val userPreference: UserPreference
) {

    fun isLogin(): Flow<String?> = flow { emitAll(userPreference.getToken()) }

    fun login(email: String, password: String): Flow<Result<String>> = flow {
        emit(Result.Loading)
        try {
            Log.d("LOGIN", "login: $userApiService $email $password")
            val response = userApiService.login(email, password)
            val token = response.data?.uid
            userPreference.saveToken(token!!)
            emit(Result.Success(response.message))
        } catch (e: Exception) {
            Log.d("UserRepository", "login: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun register(
        email: String,
        password: String,
        name: String,
    ): Flow<Result<String>> = flow {
        emit(Result.Loading)
        try {
            Log.d("register", "Register: $email pass $password name $name")
            val response = userApiService.register(email, password, name)
            val token = response.data?.uid
            userPreference.saveToken(token!!)
            emit(Result.Success(response.message))
        } catch (e: Exception) {
            Log.d("UserRepository", "register: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun reset(email: String): Flow<Result<String>> = flow {
        emit(Result.Loading)
        try {
            val response = userApiService.reset(email)
            emit(Result.Success(response.message))
        } catch (e: Exception) {
            Log.d("UserRepository", "reset: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }


    fun logout(): Flow<Result<String>> = flow {
        emit(Result.Loading)
        userPreference.logout()
        emit(Result.Success("success"))
    }

    fun getDataUser(
        token: String
    ): Flow<Result<DataUser>> = flow {
        emit(Result.Loading)
        try {
            val response = userApiService.getDataUser(token)
            emit(Result.Success(response.data!!))
        } catch (e: Exception) {
            Log.d("UserRepository", "getDataUser: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }

    }

    fun editName(
        uid: String,
        newName: String
    ): Flow<Result<String>> = flow {
        emit(Result.Loading)
        try {
            val response = userApiService.editName(
                uid = uid,
                name = newName,
            )
            emit(Result.Success(response.message))

        } catch (e: Exception) {
            Log.d("UserRepository", "editName: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun editProfilePicture(uid: String, imageFile: File): Flow<Result<String>> = flow {
        emit(Result.Loading)
        try {
            Log.d("UPLOAD", "upload uid: $userApiService $uid")
            val reducedFile = reduceFileImage(imageFile)
            val base64Image = convertImageToBase64(reducedFile)
            val response = userApiService.editProfilePicture(uid, base64Image)
            emit(Result.Success(response.message))

        } catch (e: Exception) {
            Log.d("UserRepository", "editName: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    private fun convertImageToBase64(imageFile: File): String {
        val imageBytes = imageFile.readBytes()
        val base64String = Base64.encodeToString(imageBytes, Base64.DEFAULT)
        return base64String.replace("\n", "")
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: UserApiService, userPreference: UserPreference
        ): UserRepository = instance ?: synchronized(this) {
            instance ?: UserRepository(apiService, userPreference)
        }.also { instance = it }
    }
}