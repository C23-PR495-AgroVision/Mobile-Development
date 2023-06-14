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
//            val token = response.data?.uid
//            userPreference.saveToken(token!!)
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

    fun editName(
        uid: String,
        newName: String,
//        currentName: String,
    ): Flow<Result<String>> = flow {
        emit(Result.Loading)
        try {
            val response = userApiService.editName(
                uid = uid,
                name = newName,
//                currentName = currentName
            )
            emit(Result.Success(response.message))

        } catch (e: Exception) {
            Log.d("UserRepository", "editEmailPassword: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

//    fun editProfilePicture(
//        uid: String, imageFile: File
//    ): Flow<Result<String>> = flow {
//        emit(Result.Loading)
//        try {
//            val reducedFile = reduceFileImage(imageFile)
//            val requestImageFile = reducedFile.asRequestBody("image/jpeg".toMediaType())
//            val imageMultipart: MultipartBody.Part =
//                MultipartBody.Part.createFormData("file", imageFile.name, requestImageFile)
//            val response = userApiService.editProfilePicture(
//                uid, imageMultipart
//            )
//            emit(Result.Success(response.message!!))
//
//        } catch (e: Exception) {
//            Log.d("UserRepository", "editEmailPassword: ${e.message.toString()}")
//            emit(Result.Error(e.message.toString()))
//        }
//    }

    fun editProfilePicture(uid: String, imageFile: File): Flow<Result<String>> = flow {
        emit(Result.Loading)
        try {
            Log.d("UPLOAD", "upload uid: $userApiService $uid")
            val reducedFile = reduceFileImage(imageFile)
//            val requestImageFile = reducedFile.asRequestBody("image/jpeg".toMediaType())
            val base64Image = convertImageToBase64(reducedFile)

            val response = userApiService.editProfilePicture(uid, base64Image)
            emit(Result.Success(response.message))

        } catch (e: Exception) {
            Log.d("UserRepository", "editEmailPassword: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    private fun convertImageToBase64(imageFile: File): String {
        val imageBytes = imageFile.readBytes()
        val base64String = Base64.encodeToString(imageBytes, Base64.DEFAULT)
        return base64String.replace("\n", "")
    }

//    private fun convertImageToBase64(imageFile: File): String {
//        val bitmap = BitmapFactory.decodeFile(imageFile.path)
//        val rotatedBitmap = rotateBitmap(bitmap, getRotationAngle(imageFile))
//        val outputStream = ByteArrayOutputStream()
//        rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
//        val base64String = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
//        outputStream.close()
//        return base64String.replace("\n", "")
//    }
//
//    private fun getRotationAngle(imageFile: File): Float {
//        val exif = ExifInterface(imageFile.path)
//        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
//        return when (orientation) {
//            ExifInterface.ORIENTATION_ROTATE_90 -> 90F
//            ExifInterface.ORIENTATION_ROTATE_180 -> 180F
//            ExifInterface.ORIENTATION_ROTATE_270 -> 270F
//            else -> 0F
//        }
//    }
//
//    private fun rotateBitmap(bitmap: Bitmap, angle: Float): Bitmap {
//        val matrix = Matrix()
//        matrix.postRotate(angle)
//        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
//    }

//    private fun convertImageToBase64(imageFile: File): String {
//        val bitmap = BitmapFactory.decodeFile(imageFile.path)
//        val outputStream = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
//
//        val exif = ExifInterface(imageFile.path)
//        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
//
//        val rotatedBitmap = rotateBitmap(bitmap, orientation)
//        rotatedBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
//
//        val imageBytes = outputStream.toByteArray()
//        val base64String = Base64.encodeToString(imageBytes, Base64.NO_WRAP)
//
//        try {
//            outputStream.close()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//
//        return base64String.replace("\n", "")
//    }
//
//    private fun rotateBitmap(bitmap: Bitmap, orientation: Int): Bitmap {
//        val matrix = Matrix()
//        when (orientation) {
//            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90F)
//            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180F)
//            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270F)
//        }
//        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
//    }

//    fun convertImageToStringForServer(imageBitmap: Bitmap): String? {
//        val stream = ByteArrayOutputStream()
//        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream)
//        val byteArray = stream.toByteArray()
//        return Base64.encodeToString(byteArray, Base64.DEFAULT)
//    }

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