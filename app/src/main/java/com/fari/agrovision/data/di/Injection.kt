package com.fari.agrovision.data.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.fari.agrovision.data.local.preference.UserPreference
import com.fari.agrovision.data.remote.api.ApiConfig
import com.fari.agrovision.data.remote.repository.UserRepository

object Injection {

    fun provideUserRepository(dataStore: DataStore<Preferences>): UserRepository {
        val userApiService = ApiConfig.getUserApiService()
        val authPreferences = UserPreference.getInstance(dataStore)
        return UserRepository.getInstance(userApiService, authPreferences)
    }

//    fun provideArticleRepository(): ArticleRepository {
//        val apiService = ApiConfig.getArticleApiService()
//        return ArticleRepository.getInstance(apiService)
//    }
//
//    fun provideDetectionRepository(): DetectionRepository {
//        val apiService = ApiConfig.getDetectionApiService()
//        return DetectionRepository.getInstance(apiService)
//    }
}