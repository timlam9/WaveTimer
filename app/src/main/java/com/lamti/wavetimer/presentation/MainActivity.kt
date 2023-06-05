package com.lamti.wavetimer.presentation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.FragmentActivity
import androidx.wear.ambient.AmbientModeSupport
import com.lamti.wavetimer.data.CoroutinesTimerViewModel
import com.lamti.wavetimer.presentation.theme.WaveTimerTheme
import com.lamti.wavetimer.presentation.timer.WaveTimer

class MainActivity : FragmentActivity(), AmbientModeSupport.AmbientCallbackProvider {

    private lateinit var ambientController: AmbientModeSupport.AmbientController

    override fun getAmbientCallback(): AmbientModeSupport.AmbientCallback = MyAmbientCallback()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val height = resources.configuration.screenHeightDp
        ambientController = AmbientModeSupport.attach(this@MainActivity)

        setContent {
            val viewModel by viewModels<CoroutinesTimerViewModel>()
            val timerState = viewModel.timerStateFlow.collectAsState()

            WearApp(
                ambientController.isAmbient,
                height = height,
                currentTime = timerState.value.formattedTime,
                progress = timerState.value.progressPercentage,
                isTimerRunning = timerState.value.isRunning,
                onResetClick = viewModel::resetTimer,
                onPlayClick = viewModel::toggleStart
            )
        }
    }
}

@Composable
fun WearApp(
    isInAmbientState: Boolean,
    height: Int,
    currentTime: String,
    progress: Float,
    isTimerRunning: Boolean,
    onResetClick: () -> Unit,
    onPlayClick: () -> Unit
) {
    WaveTimerTheme {
        WaveTimer(
            isInAmbientState = isInAmbientState,
            screenHeight = height,
            currentTime = currentTime,
            progress = progress,
            isTimerRunning = isTimerRunning,
            onResetClick = onResetClick,
            onPlayClick = onPlayClick,
        )
    }
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    WearApp(
        isInAmbientState = false,
        height = 300,
        currentTime = "",
        progress = 0f,
        isTimerRunning = false,
        onResetClick = {}
    ) {}
}