package com.example.p_9


import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.emailDataStore by preferencesDataStore(name = "user_preferences")

object EmailDataStore {
    private val EMAIL_KEY = stringPreferencesKey("email")

    suspend fun setEmail(context: Context, email: String) {
        context.emailDataStore.edit { preferences ->
            preferences[EMAIL_KEY] = email
        }
    }

    fun getEmail(context: Context): Flow<String?> {
        return context.emailDataStore.data.map { preferences ->
            preferences[EMAIL_KEY]
        }
    }
}
