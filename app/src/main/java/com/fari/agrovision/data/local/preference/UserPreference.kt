package com.fari.agrovision.data.local.preference

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    fun getToken(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[tokenKey]
        }
    }

    suspend fun saveToken(token: String) {
        Log.i("TOKEN USER",token)
        dataStore.edit { preferences ->
            preferences[tokenKey] = token
        }
    }

    suspend fun logout() {
        dataStore.edit {
            it.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null
        private val tokenKey = stringPreferencesKey("token")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}