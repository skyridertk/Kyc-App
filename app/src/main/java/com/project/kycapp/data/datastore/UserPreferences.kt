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

    fun sipFlow(): Flow<String> =
        context.dataStore.data
            .map { preferences ->
                preferences[SIP_USER_KEY] ?: ""
            }

    suspend fun setToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    suspend fun setSipUser(user: String) {
        context.dataStore.edit { preferences ->
            preferences[SIP_USER_KEY] = user
        }
    }

    companion object {
        val TOKEN_KEY = stringPreferencesKey("token_key")
        val SIP_USER_KEY = stringPreferencesKey("sip_user_key")
    }
}

