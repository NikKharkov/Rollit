package com.hi88.app.generate_number

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hi88.app.history.data.model.HistoryItem
import com.hi88.app.history.data.model.SavedRange
import com.hi88.app.history.data.repository.HistoryRepository
import com.hi88.app.history.data.repository.SavedRangesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.random.Random

class GenerateNumberViewModel(
    private val savedRangesRepository: SavedRangesRepository,
    private val historyRepository: HistoryRepository,
    private val dataStore: DataStore<Preferences>,
) : ViewModel() {

    private val _numberCount = MutableStateFlow("1")
    val numberCount: StateFlow<String> = _numberCount

    private val _rangeFrom = MutableStateFlow("1")
    val rangeFrom: StateFlow<String> = _rangeFrom

    private val _rangeTo = MutableStateFlow("100")
    val rangeTo: StateFlow<String> = _rangeTo

    private val _numbers = MutableStateFlow<List<Int>>(emptyList())
    val numbers: StateFlow<List<Int>> = _numbers

    private val _noRepetitions = MutableStateFlow(false)
    val noRepetitions: StateFlow<Boolean> = _noRepetitions

    private val _saveRange = MutableStateFlow(false)
    val saveRange: StateFlow<Boolean> = _saveRange

    private val KEY_SAVE_RANGE = booleanPreferencesKey("save_range")
    private val KEY_RANGE_FROM = stringPreferencesKey("range_from")
    private val KEY_RANGE_TO = stringPreferencesKey("range_to")

    init {
        viewModelScope.launch {
            val preferences = dataStore.data.first()
            _saveRange.value = preferences[KEY_SAVE_RANGE] ?: false
            if (_saveRange.value) {
                _rangeFrom.value = preferences[KEY_RANGE_FROM] ?: "1"
                _rangeTo.value = preferences[KEY_RANGE_TO] ?: "100"
            }

            _saveRange.collectLatest { shouldSave ->
                if (shouldSave) {
                    saveRangeToDataStore()
                }
            }
        }
    }

    fun saveCurrentRangeAsPreset(name: String = "${_rangeFrom.value}–${_rangeTo.value}") {
        val from = _rangeFrom.value.toIntOrNull() ?: return
        val to = _rangeTo.value.toIntOrNull() ?: return
        if (from >= to) return

        viewModelScope.launch(Dispatchers.IO) {
            savedRangesRepository.addSavedRange(
                SavedRange(
                    name = name,
                    rangeFrom = from,
                    rangeTo = to
                )
            )
        }
    }

    fun applyPreset(range: SavedRange) {
        _rangeFrom.value = range.rangeFrom.toString()
        _rangeTo.value = range.rangeTo.toString()
        generateNumbers()
    }

    fun onNumberCountChange(value: String) {
        _numberCount.value = value
    }

    fun onRangeFromChange(value: String) {
        _rangeFrom.value = value
        if (_saveRange.value) saveRangeToDataStore()
    }

    fun onRangeToChange(value: String) {
        _rangeTo.value = value
        if (_saveRange.value) saveRangeToDataStore()
    }

    fun onNoRepetitionsChange(value: Boolean) {
        _noRepetitions.value = value
    }

    fun onSaveRangeChange(value: Boolean) {
        _saveRange.value = value
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[KEY_SAVE_RANGE] = value
                if (!value) {
                    preferences.remove(KEY_RANGE_FROM)
                    preferences.remove(KEY_RANGE_TO)
                }
            }
        }
    }

    private fun saveRangeToDataStore() {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[KEY_RANGE_FROM] = _rangeFrom.value
                preferences[KEY_RANGE_TO] = _rangeTo.value
            }
        }
    }

    fun generateNumbers() {
        val count = _numberCount.value.toIntOrNull() ?: 1
        val from = _rangeFrom.value.toIntOrNull() ?: 1
        val to = _rangeTo.value.toIntOrNull() ?: 100

        if (from >= to || count <= 0) {
            _numbers.value = emptyList()
            return
        }

        if (_noRepetitions.value) {
            val rangeSize = to - from + 1
            if (rangeSize < count) {
                _numbers.value = emptyList()
                return
            }
            _numbers.value = (from..to).shuffled().take(count)
        } else {
            _numbers.value = List(count) { Random.nextInt(from, to + 1) }
        }
        viewModelScope.launch(Dispatchers.IO) {
            val resultText = "Range $from–$to, result ${_numbers.value.joinToString(", ")}"
            historyRepository.addHistoryItem(HistoryItem("Number", resultText))
        }
    }
}