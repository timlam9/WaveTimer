package com.lamti.wavetimer.presentation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import kotlinx.coroutines.delay

private val path = Path()

@Composable
fun WaveTimer(
    totalTime: Long,
    modifier: Modifier = Modifier,
    targetColor: Color = MaterialTheme.colors.primary,
    initialValue: Float = 1f,
    waveHeight: Float = 80f,
    screenHeight: Int = 900,
    onComplete: () -> Unit,
) {
    val screenHeightPx = with(LocalDensity.current) { screenHeight * density }

    var translationY by remember { mutableStateOf(screenHeightPx) }
    var progress by remember { mutableStateOf(initialValue) }
    var currentTime by remember { mutableStateOf(totalTime) }
    var isTimerRunning by remember { mutableStateOf(false) }
    var resetTimer by remember { mutableStateOf(false) }

    val deltaXAnim = rememberInfiniteTransition()
    val dx by deltaXAnim.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(durationMillis = 300, easing = LinearEasing))
    )

    val animWaveHeight by animateFloatAsState(
        targetValue = waveHeight,
        animationSpec = TweenSpec(durationMillis = 300, easing = LinearEasing),
    )
    
    LaunchedEffect(currentTime, isTimerRunning, resetTimer) {
        if (resetTimer) {
            resetTimer = false
            progress = initialValue
            translationY = screenHeightPx
            currentTime = totalTime
        }

        if (currentTime > 0L && isTimerRunning) {
            delay(100L)
            currentTime -= 100L
            progress = currentTime / totalTime.toFloat()
            translationY = progress * screenHeightPx
        }

        if (currentTime == 0L) {
            onComplete()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Canvas(
            modifier = Modifier.fillMaxSize(),
            onDraw = {
                translate(top = translationY) {
                    waveUI(
                        path = path,
                        color = targetColor,
                        dx = dx,
                        waveHeight = (progress * animWaveHeight) + animWaveHeight / 3
                    )
                }
            }
        )
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = (currentTime / 1000L).toString(),
            fontSize = 84.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Row(
            modifier = modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = { isTimerRunning = !isTimerRunning }) {
                Text(text = if (isTimerRunning) "Stop" else "Start")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = {
                    isTimerRunning = false
                    resetTimer = true
                }
            ) {
                Text(text = "reset")
            }
        }
    }
}

private fun DrawScope.waveUI(path: Path, color: Color, dx: Float, waveHeight: Float) {
    val waveWidth = 600
    val originalY = 0f

    drawPath(path, color)
    path.reset()

    val halfWaveWidth = waveWidth / 2

    path.moveTo(x = -waveWidth + (waveWidth * dx), y = originalY.dp.toPx())

    for (i in -waveWidth..(size.width.toInt() + waveWidth) step waveWidth) {
        path.relativeQuadraticBezierTo(
            dx1 = halfWaveWidth.toFloat() / 2,
            dy1 = -waveHeight,
            dx2 = halfWaveWidth.toFloat(),
            dy2 = 0f
        )
        path.relativeQuadraticBezierTo(
            dx1 = halfWaveWidth.toFloat() / 2,
            dy1 = waveHeight,
            dx2 = halfWaveWidth.toFloat(),
            dy2 = 0f
        )
    }

    path.lineTo(size.width, size.height)
    path.lineTo(0f, size.height)
    path.close()
}
