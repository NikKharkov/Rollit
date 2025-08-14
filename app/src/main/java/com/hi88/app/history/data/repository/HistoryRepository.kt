package com.hi88.app.history.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.hi88.app.history.data.model.HistoryItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class HistoryRepository(
    private val dataStore: DataStore<Preferences>
) {
    private val maxItems = 5
    private val itemSeparator = "|||"
    private val fieldSeparator = ":::"

    suspend fun addHistoryItem(item: HistoryItem) {
        dataStore.edit { preferences ->
            val currentItems = getHistoryItems(preferences)
            val updatedItems = (listOf(item) + currentItems)
                .sortedByDescending { it.timestamp }
                .take(maxItems)
            val itemsString = updatedItems.joinToString(itemSeparator) {
                "${it.type}$fieldSeparator${it.result}$fieldSeparator${it.timestamp}"
            }
            preferences[HistoryKeys.HISTORY_ITEMS] = itemsString
        }
    }

    suspend fun removeHistoryItem(timestamp: Long) {
        dataStore.edit { preferences ->
            val currentItems = getHistoryItems(preferences)
            val updatedItems = currentItems.filter { it.timestamp != timestamp }
            val itemsString = updatedItems.joinToString(itemSeparator) {
                "${it.type}$fieldSeparator${it.result}$fieldSeparator${it.timestamp}"
            }
            preferences[HistoryKeys.HISTORY_ITEMS] = itemsString.ifEmpty { "" }
        }
    }

    fun getHistoryFlow(): Flow<List<HistoryItem>> = dataStore.data
        .map { preferences ->
            getHistoryItems(preferences)
        }

    private fun getHistoryItems(preferences: Preferences): List<HistoryItem> {
        val itemsString = preferences[HistoryKeys.HISTORY_ITEMS] ?: return emptyList()
        if (itemsString.isEmpty()) return emptyList()

        return itemsString.split(itemSeparator).mapNotNull { item ->
            val parts = item.split(fieldSeparator)
            if (parts.size == 3) {
                HistoryItem(
                    type = parts[0],
                    result = parts[1],
                    timestamp = parts[2].toLongOrNull() ?: return@mapNotNull null
                )
            } else null
        }
    }
}

object HistoryKeys {
    val HISTORY_ITEMS = stringPreferencesKey("history_items")
}