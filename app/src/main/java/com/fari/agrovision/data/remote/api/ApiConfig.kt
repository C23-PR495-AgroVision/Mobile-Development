package com.fari.agrovision.data.remote.api

import androidx.viewbinding.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiConfig {
    companion object {
        fun getUserApiService(): UserApiService {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.HEADERS
                level = HttpLoggingInterceptor.Level.BODY
            }
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://capstone-project-386912.et.r.appspot.com")
                .baseUrl("http://192.168.100.205:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(UserApiService::class.java)
        }

//        fun getArticleApiService(): ArticleApiService {
//            val loggingInterceptor = if (BuildConfig.DEBUG) {
//                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
//            } else {
//                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
//            }
//            val client = OkHttpClient.Builder()
//                .addInterceptor(loggingInterceptor)
//                .build()
//            val retrofit = Retrofit.Builder()
//                .baseUrl("https://article-api-47y47bgawa-et.a.run.app/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .client(client)
//                .build()
//            return retrofit.create(ArticleApiService::class.java)
//        }
//
//        fun getDetectionApiService(): DetectionApiService{
//            val loggingInterceptor = if (BuildConfig.DEBUG) {
//                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
//            } else {
//                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
//            }
//            val client = OkHttpClient.Builder()
//                .addInterceptor(loggingInterceptor)
//                .connectTimeout(60, TimeUnit.SECONDS)
//                .readTimeout(60, TimeUnit.SECONDS)
//                .writeTimeout(60, TimeUnit.SECONDS)
//                .build()
//            val retrofit = Retrofit.Builder()
//                .baseUrl("https://skin-disease-api-47y47bgawa-et.a.run.app/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .client(client)
//                .build()
//            return retrofit.create(DetectionApiService::class.java)
//        }
    }
}