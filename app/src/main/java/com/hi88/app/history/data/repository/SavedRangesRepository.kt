package com.hi88.app.history.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.hi88.app.history.data.model.SavedRange
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SavedRangesRepository(
    private val dataStore: DataStore<Preferences>
) {
    private val maxPresets = 5
    private val itemSeparator = "|||"
    private val fieldSeparator = ":::"

    companion object {
        val SAVED_RANGES = stringPreferencesKey("saved_ranges")
    }

    suspend fun addSavedRange(range: SavedRange) {
        dataStore.edit { preferences ->
            val currentRanges = getSavedRanges(preferences)
            val updatedRanges = (listOf(range) + currentRanges)
                .sortedByDescending { it.timestamp }
                .take(maxPresets)
            val rangesString = updatedRanges.joinToString(itemSeparator) {
                "${it.name}$fieldSeparator${it.rangeFrom}$fieldSeparator${it.rangeTo}$fieldSeparator${it.timestamp}"
            }
            preferences[SAVED_RANGES] = rangesString
        }
    }

    suspend fun removeSavedRange(timestamp: Long) {
        dataStore.edit { preferences ->
            val currentRanges = getSavedRanges(preferences)
            val updatedRanges = currentRanges.filter { it.timestamp != timestamp }
            val rangesString = updatedRanges.joinToString(itemSeparator) {
                "${it.name}$fieldSeparator${it.rangeFrom}$fieldSeparator${it.rangeTo}$fieldSeparator${it.timestamp}"
            }
            preferences[SAVED_RANGES] = rangesString.ifEmpty { "" }
        }
    }

    fun getSavedRangesFlow(): Flow<List<SavedRange>> = dataStore.data
        .map { preferences ->
            getSavedRanges(preferences)
        }

    private fun getSavedRanges(preferences: Preferences): List<SavedRange> {
        val rangesString = preferences[SAVED_RANGES] ?: return emptyList()
        if (rangesString.isEmpty()) return emptyList()

        return rangesString.split(itemSeparator).mapNotNull { range ->
            val parts = range.split(fieldSeparator)
            if (parts.size == 4) {
                SavedRange(
                    name = parts[0],
                    rangeFrom = parts[1].toIntOrNull() ?: return@mapNotNull null,
                    rangeTo = parts[2].toIntOrNull() ?: return@mapNotNull null,
                    timestamp = parts[3].toLongOrNull() ?: return@mapNotNull null
                )
            } else null
        }
    }
}