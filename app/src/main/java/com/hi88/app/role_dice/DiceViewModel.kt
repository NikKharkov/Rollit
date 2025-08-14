package com.hi88.app.role_dice

import androidx.lifecycle.ViewModel
import com.hi88.app.history.data.model.HistoryItem
import com.hi88.app.history.data.repository.HistoryRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.random.Random

class DiceViewModel(
    private val historyRepository: HistoryRepository
) : ViewModel() {

    private var _dice = MutableStateFlow(listOf(1))
    val dice = _dice.asStateFlow()

    private val _isRolling = MutableStateFlow(false)
    val isRolling = _isRolling.asStateFlow()

    private var _diceCount = MutableStateFlow(1)

    fun setDiceCount(count: Int) {
        _diceCount.value = count
        _dice.value = List(count) { 1 }
    }

    suspend fun rollDice() {
        _isRolling.value = true
        delay(2000)
        _dice.value = List(_diceCount.value) { Random.nextInt(1, 7) }
        _isRolling.value = false

        val diceCount = _diceCount.value
        val results = _dice.value.joinToString(", ")
        val sum = _dice.value.sum()
        val resultText = "${diceCount}D6 → $results; amount $sum"
        historyRepository.addHistoryItem(HistoryItem("Dice", resultText))
    }
}
