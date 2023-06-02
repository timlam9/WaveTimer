package com.lamti.wavetimer.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.lamti.wavetimer.presentation.theme.WaveTimerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val height = resources.configuration.screenHeightDp

        setContent {
            WearApp(height)
        }
    }
}

@Composable
fun WearApp(height: Int) {
    WaveTimerTheme {
        WaveTimer(
            totalTime = 10_000,
            screenHeight = height,
            onComplete = {}
        )
    }
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    WearApp(300)
}