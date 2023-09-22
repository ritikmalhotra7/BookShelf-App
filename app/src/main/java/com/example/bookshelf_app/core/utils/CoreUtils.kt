package com.example.bookshelf_app.core.utils

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

object CoreUtils {
    const val BASE_URL = "https://www.jsonkeeper.com"

    private const val PREFERENCES_NAME = "BookShelfPreferences"
    val Context.dataStore by preferencesDataStore(name = PREFERENCES_NAME)

    const val KEY_FOR_LOGGED_IN_USER = "Logged In User Details"

    suspend fun Context.saveToDataStore(key: String, data: String) {
        val key = stringPreferencesKey(key)
        this.dataStore.edit { preferences ->
            preferences[key] = data
        }
    }
}