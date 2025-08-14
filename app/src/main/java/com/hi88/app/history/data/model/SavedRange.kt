package com.hi88.app.history.data.model

data class SavedRange(
    val name: String,
    val rangeFrom: Int,
    val rangeTo: Int,
    val timestamp: Long = System.currentTimeMillis()
)