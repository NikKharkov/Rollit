package com.hi88.app.list.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ListItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String
)

