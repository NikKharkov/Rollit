package com.hi88.app.history.data.model

data class HistoryItem(
    val type: String,
    val result: String,
    val timestamp: Long = System.currentTimeMillis()
)
