package com.hi88.app.list.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = ListItem::class,
            parentColumns = ["id"],
            childColumns = ["listId"],
            onDelete = CASCADE
        )
    ]
)
data class ListEntry(
    @PrimaryKey(autoGenerate = true) val entryId: Int = 0,
    val listId: Int,
    val content: String
)