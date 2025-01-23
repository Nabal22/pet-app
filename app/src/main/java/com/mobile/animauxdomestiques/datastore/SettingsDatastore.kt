package com.mobile.animauxdomestiques.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.mobile.animauxdomestiques.navigation.Screen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = Screen.SettingsScreen.route)

object SettingsPreferencesKeys {
    val DARK_MODE = booleanPreferencesKey("dark_mode")
    val FONT_SIZE = floatPreferencesKey("font_size")
}

class SettingsDataStore(private val context: Context) {

    val darkMode: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[SettingsPreferencesKeys.DARK_MODE] ?: false
    }

    val fontSize: Flow<Float> = context.dataStore.data.map { preferences ->
        preferences[SettingsPreferencesKeys.FONT_SIZE] ?: 16f
    }

    suspend fun setDarkTheme(isDarkMode: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[SettingsPreferencesKeys.DARK_MODE] = isDarkMode
        }
    }

    suspend fun setFontSize(fontSize: Float) {
        context.dataStore.edit { preferences ->
            preferences[SettingsPreferencesKeys.FONT_SIZE] = fontSize
        }
    }
}
