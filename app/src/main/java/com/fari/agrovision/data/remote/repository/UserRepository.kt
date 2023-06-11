package com.fari.agrovision.data.remote.repository

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
import java.io.File

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
            Log.d("LOGIN", "login: $userApiService $email $password")
            val token = response.data?.uid
            Log.d("LOGIN", "login: $userApiService $email $password")
            userPreference.saveToken(token!!)
            Log.d("LOGIN", "login: $userApiService $email $password")
            emit(Result.Success(response.message))
            Log.d("LOGIN", "login: $userApiService $email $password")
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
            val token = response.data?.uid
            userPreference.saveToken(token!!)
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
//
//    fun editEmailPassword(
//        token: String,
//        currentEmail: String,
//        newEmail: String,
//        currentPassword: String,
//        newPassword: String
//    ): Flow<Result<String>> = flow {
//        emit(Result.Loading)
//        try {
//            val response = userApiService.editEmailPassword(
//                uid = token,
//                email = newEmail,
//                password = newPassword,
//                currentEmail = currentEmail,
//                currentPassword = currentPassword
//            )
//            emit(Result.Success(response.message!!))
//
//        } catch (e: Exception) {
//            Log.d("UserRepository", "editEmailPassword: ${e.message.toString()}")
//            emit(Result.Error(e.message.toString()))
//        }
//    }
//
    fun editName(
        uid: String,
        newName: String,
        currentName: String,
    ): Flow<Result<String>> = flow {
        emit(Result.Loading)
        try {
            val response = userApiService.editName(
                uid = uid,
                name = newName,
                currentName = currentName
            )
            emit(Result.Success(response.message!!))

        } catch (e: Exception) {
            Log.d("UserRepository", "editEmailPassword: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun editProfilePicture(
        uid: String, imageFile: File
    ): Flow<Result<String>> = flow {
        emit(Result.Loading)
        try {
            val reducedFile = reduceFileImage(imageFile)
            val requestImageFile = reducedFile.asRequestBody("image/jpeg".toMediaType())
            val imageMultipart: MultipartBody.Part =
                MultipartBody.Part.createFormData("file", imageFile.name, requestImageFile)
            val response = userApiService.editProfilePicture(
                uid, imageMultipart
            )
            emit(Result.Success(response.message!!))

        } catch (e: Exception) {
            Log.d("UserRepository", "editEmailPassword: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
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