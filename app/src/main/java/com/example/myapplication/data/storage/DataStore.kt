package com.example.myapplication.data.storage


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStore(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("userData")
        private val USER_DATA_KEY = stringPreferencesKey("user_data")
    }
    val getData: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[USER_DATA_KEY] ?: ""
    }
    suspend fun saveData(data: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_DATA_KEY] = data
        }
    }
}