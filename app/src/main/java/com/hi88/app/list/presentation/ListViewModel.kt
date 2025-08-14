package com.hi88.app.list.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hi88.app.list.data.entity.ListEntry
import com.hi88.app.list.data.entity.ListItem
import com.hi88.app.list.data.repository.ListRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ListViewModel(
    private val listRepository: ListRepository
) : ViewModel() {

    private val _lists = MutableStateFlow<List<ListItem>>(emptyList())
    val lists = _lists.asStateFlow()

    private val _currentEntries = MutableStateFlow<List<ListEntry>>(emptyList())
    val currentEntries = _currentEntries.asStateFlow()

    init {
        viewModelScope.launch {
            listRepository.getAllLists().collect { lists ->
                _lists.value = lists
            }
        }
    }

    fun loadEntriesForList(listId: Int) {
        viewModelScope.launch {
            listRepository.getEntriesForList(listId).collect { entries ->
                _currentEntries.value = entries
            }
        }
    }

    fun addList(title: String) {
        viewModelScope.launch {
            listRepository.insertList(ListItem(title = title))
        }
    }

    fun deleteList(list: ListItem) {
        viewModelScope.launch {
            listRepository.deleteList(list)
        }
    }

    fun updateList(list: ListItem) {
        viewModelScope.launch {
            listRepository.updateList(list)
        }
    }

    fun addEntry(listId: Int, content: String) {
        viewModelScope.launch {
            listRepository.insertEntry(ListEntry(listId = listId, content = content))
        }
    }

    fun deleteEntry(entry: ListEntry) {
        viewModelScope.launch {
            listRepository.deleteEntry(entry)
        }
    }

    fun shuffleEntries(listId: Int) {
        viewModelScope.launch {
            val currentEntries = _currentEntries.value.toMutableList()
            currentEntries.shuffle()
            _currentEntries.value = currentEntries
        }
    }

    fun selectRandomEntry(excludeSelected: Boolean, selectedEntries: Set<Int>): ListEntry? {
        val availableEntries = _currentEntries.value.filter { entry ->
            !excludeSelected || !selectedEntries.contains(entry.entryId)
        }
        return if (availableEntries.isNotEmpty()) {
            availableEntries.random()
        } else {
            null
        }
    }

    fun deleteSelectedEntries(listId: Int, selectedEntries: Set<Int>) {
        viewModelScope.launch {
            val entriesToDelete = _currentEntries.value.filter { selectedEntries.contains(it.entryId) }
            entriesToDelete.forEach { entry ->
                listRepository.deleteEntry(entry)
            }
            loadEntriesForList(listId)
        }
    }
}
