package com.fari.agrovision.data.remote.api

import com.fari.agrovision.data.remote.model.auth.AuthResponse
import com.fari.agrovision.data.remote.model.auth.UserResponse
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

    @FormUrlEncoded
    @POST("user/{uid}/profile-picture")
    suspend fun editProfilePicture(
        @Path("uid") uid: String,
        @Field("profilePicture") file: String
    ): UserResponse

    @FormUrlEncoded
    @PUT("user/{uid}/name")
    suspend fun editName(
        @Path("uid") uid: String,
        @Field("name") name: String
    ): UserResponse

}