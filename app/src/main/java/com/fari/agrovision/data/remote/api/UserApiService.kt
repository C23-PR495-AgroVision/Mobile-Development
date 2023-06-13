package com.fari.agrovision.data.remote.api

import com.fari.agrovision.data.remote.model.auth.AuthResponse
import com.fari.agrovision.data.remote.model.auth.UserResponse
import okhttp3.MultipartBody
import retrofit2.http.*

interface UserApiService {

    @FormUrlEncoded
    @POST("userHandler")
    suspend fun register(
        @Field("emailField") email: String,
        @Field("passwordField") password: String,
        @Field("fullnameField") name: String
    ): AuthResponse

    @FormUrlEncoded
    @POST("signin")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): AuthResponse

    @FormUrlEncoded
    @POST("reset-password")
    suspend fun reset(
        @Field("email") email: String
    ): AuthResponse

    @GET("user/{uid}")
    suspend fun getDataUser(
        @Path("uid") uid: String
    ): UserResponse

//    @Multipart
    @FormUrlEncoded
    @POST("user/{uid}/profile-picture")
    suspend fun editProfilePicture(
        @Field("uid") uid: String,
        @Field("profilePicture") file: String
//        @Part file: MultipartBody.Part,
    ): UserResponse

    @FormUrlEncoded
    @PUT("user/{uid}/name")
    suspend fun editName(
        @Field("uid") uid: String,
        @Field("name") name: String,
//        @Field("currentName") currentName: String,
    ): UserResponse

//    @FormUrlEncoded
//    @PUT("edit-email/{uid}")
//    suspend fun editEmailPassword(
//        @Path("uid") uid: String,
//        @Field("email") email: String,
//        @Field("password") password: String,
//        @Field("currentEmail") currentEmail: String,
//        @Field("currentPassword") currentPassword: String,
//    ): UserResponse
}