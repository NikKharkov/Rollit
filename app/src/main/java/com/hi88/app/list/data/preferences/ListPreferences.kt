package com.hi88.app.list.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.listDataStore: DataStore<Preferences> by preferencesDataStore(name = "list_preferences")

val SELECTED_ENTRIES_KEY = stringSetPreferencesKey("selected_entries")

fun Context.getSelectedEntries(): Flow<Set<Int>> {
    return listDataStore.data.map { preferences ->
        preferences[SELECTED_ENTRIES_KEY]?.map { it.toInt() }?.toSet() ?: emptySet()
    }
}

suspend fun Context.saveSelectedEntries(selectedEntries: Set<Int?>) {
    listDataStore.edit { preferences ->
        preferences[SELECTED_ENTRIES_KEY] = selectedEntries.map { it.toString() }.toSet()
    }
}