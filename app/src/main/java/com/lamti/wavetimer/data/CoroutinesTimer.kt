package com.lamti.wavetimer.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TOTAL_TIME = 60_000L

class CoroutinesTimer(private val timerScope: CoroutineScope) {

    private var _timerStateFlow = MutableStateFlow(CoroutinesTimerState(TOTAL_TIME))
    val timerStateFlow: StateFlow<CoroutinesTimerState> = _timerStateFlow

    init {
        timerScope.launch {
            _timerStateFlow.onEach { state ->
                if (state.isRunning) {
                    val newTime = _timerStateFlow.value.timeRemaining - 1000L
                    _timerStateFlow.update { it.copy(timeRemaining = newTime) }

                    delay(1000L)
                }
            }.launchIn(timerScope)
        }
    }

    fun toggleStart() {
        _timerStateFlow.update {
            it.copy(isRunning = !it.isRunning)
        }
    }

    fun reset() {
        _timerStateFlow.update {
            it.copy(isRunning = false, timeRemaining = it.totalTime)
        }
    }
}