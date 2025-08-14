package com.hi88.app.list.data.repository

import com.hi88.app.list.data.dao.ListDao
import com.hi88.app.list.data.entity.ListEntry
import com.hi88.app.list.data.entity.ListItem
import kotlinx.coroutines.flow.Flow

class ListRepository(
    private val listDao: ListDao
) {

    fun getAllLists(): Flow<List<ListItem>> = listDao.getAllLists()

    suspend fun insertList(list: ListItem) = listDao.insertList(list)

    suspend fun deleteList(list: ListItem) = listDao.deleteList(list)

    suspend fun updateList(list: ListItem) = listDao.updateList(list)

    fun getEntriesForList(listId: Int): Flow<List<ListEntry>> = listDao.getEntriesForList(listId)

    suspend fun insertEntry(entry: ListEntry) = listDao.insertEntry(entry)

    suspend fun deleteEntry(entry: ListEntry) = listDao.deleteEntry(entry)

}