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

    @Multipart
    @POST("user/{uid}/profile-picture")
    suspend fun editProfilePicture(
        @Path("uid") uid: String,
        @Part file: MultipartBody.Part,
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

    @FormUrlEncoded
    @PUT("user/{uid}/name")
    suspend fun editName(
        @Path("uid") uid: String,
        @Field("name") name: String,
        @Field("currentName") currentName: String,
    ): UserResponse

//    @GET("stories")
//    suspend fun getAllStories(
//        @Header("Authorization") token: String,
//        @Query("page") page: Int? = null,
//        @Query("size") size: Int? = null,
//        @Query("location") location: Int = 0
//    ): AllStoriesResponse
//
//    @GET("stories")
//    suspend fun getAllStoriesWithLocation(
//        @Header("Authorization") token: String,
//        @Query("page") page: Int? = null,
//        @Query("size") size: Int? = 30,
//        @Query("location") location: Int = 1
//    ): AllStoriesResponse
//
//    @GET("stories/{id}")
//    suspend fun getDetailStory(
//        @Header("Authorization") token: String,
//        @Path("id") id: String
//    ): DetailStoryResponse
//
//    @Multipart
//    @POST("stories")
//    suspend fun addStory(
//        @Header("Authorization") token: String,
//        @Part file: MultipartBody.Part,
//        @Part("description") description: RequestBody,
//        @Part("lat") lat: RequestBody? = null,
//        @Part("lon") lon: RequestBody? = null
//    ): AddStoryResponse
}