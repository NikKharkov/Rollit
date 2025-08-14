package com.hi88.app.list.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.hi88.app.list.data.entity.ListEntry
import com.hi88.app.list.data.entity.ListItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ListDao {

    @Query("SELECT * FROM ListItem")
    fun getAllLists(): Flow<List<ListItem>>

    @Insert
    suspend fun insertList(list: ListItem)

    @Delete
    suspend fun deleteList(list: ListItem)

    @Update
    suspend fun updateList(list: ListItem)

    @Query("SELECT * FROM ListEntry WHERE listId = :listId")
    fun getEntriesForList(listId: Int): Flow<List<ListEntry>>

    @Insert
    suspend fun insertEntry(entry: ListEntry)

    @Delete
    suspend fun deleteEntry(entry: ListEntry)

}