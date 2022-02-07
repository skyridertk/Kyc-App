package com.project.kycapp.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "custom_prefs")

class UserPreferences(private val context: Context) {

    fun tokenFlow(): Flow<String> =
        context.dataStore.data
            .map { preferences ->
                preferences[TOKEN_KEY] ?: ""
            }

    fun emailFlow(): Flow<String> =
        context.dataStore.data
            .map { preferences ->
                preferences[EMAIL_KEY] ?: ""
            }

    suspend fun setToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    suspend fun setEmail(email: String) {
        context.dataStore.edit { preferences ->
            preferences[EMAIL_KEY] = email
        }
    }

    companion object {
        val TOKEN_KEY = stringPreferencesKey("token_key")
        val EMAIL_KEY = stringPreferencesKey("email_key")
    }
}

