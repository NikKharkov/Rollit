package com.hi88.app.settings.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepository(
    private val dataStore: DataStore<Preferences>,
) {
    companion object {
        val IS_DARK_THEME = booleanPreferencesKey("is_dark_theme")
        val ARE_ANIMATIONS_ENABLED = booleanPreferencesKey("are_animations_enabled")
        val IS_SOUND_ENABLED = booleanPreferencesKey("is_sound_enabled")
    }

    val isDarkThemeFlow: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[IS_DARK_THEME] ?: false
        }

    val areAnimationsEnabledFlow: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[ARE_ANIMATIONS_ENABLED] ?: true
        }

    val isSoundEnabledFlow: Flow<Boolean> = dataStore.data
        .map { preferences -> preferences[IS_SOUND_ENABLED] ?: true }

    suspend fun setDarkTheme(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_DARK_THEME] = enabled
        }
    }

    suspend fun setAnimationsEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[ARE_ANIMATIONS_ENABLED] = enabled
        }
    }

    suspend fun setSoundEnabled(enabled: Boolean) {
        dataStore.edit { preferences -> preferences[IS_SOUND_ENABLED] = enabled }
    }
}