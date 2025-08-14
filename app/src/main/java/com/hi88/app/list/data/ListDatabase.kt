package com.hi88.app.list.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hi88.app.list.data.dao.ListDao
import com.hi88.app.list.data.entity.ListEntry
import com.hi88.app.list.data.entity.ListItem

@Database(
    version = 1,
    entities = [ListItem::class, ListEntry::class]
)
abstract class ListDatabase : RoomDatabase() {

    abstract fun listDao() : ListDao

}