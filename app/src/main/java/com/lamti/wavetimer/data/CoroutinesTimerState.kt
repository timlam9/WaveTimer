package com.lamti.wavetimer.data

data class CoroutinesTimerState(
    val totalTime: Long,
    val timeRemaining: Long = totalTime,
    val isRunning: Boolean = false,
) {
    val progressPercentage: Float = timeRemaining / totalTime.toFloat()

    val formattedTime = timeRemaining.formattedTime()

    private fun Long.formattedTime(): String {
        val currentSeconds = this / 1000
        val hours: Long = currentSeconds / 3600
        val minutes: Long = currentSeconds % 3600 / 60
        val seconds: Long = currentSeconds % 60
        return "$minutes:$seconds"
    }
}