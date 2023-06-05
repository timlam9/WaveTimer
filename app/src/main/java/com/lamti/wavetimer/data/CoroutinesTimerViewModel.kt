package com.lamti.wavetimer.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.StateFlow

class CoroutinesTimerViewModel : ViewModel() {

    private val timer = CoroutinesTimer(viewModelScope)
    val timerStateFlow: StateFlow<CoroutinesTimerState> = timer.timerStateFlow

    fun toggleStart() {
        timer.toggleStart()
    }

    fun resetTimer() {
        timer.reset()
    }
}