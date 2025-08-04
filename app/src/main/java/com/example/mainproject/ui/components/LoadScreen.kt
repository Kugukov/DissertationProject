package com.example.mainproject.ui.components

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mainproject.ui.theme.MainProjectTheme
import com.example.mainproject.viewmodel.AudioTalesViewModel
import com.example.mainproject.viewmodel.MainViewModel
import com.example.mainproject.viewmodel.TextViewModel
import kotlinx.coroutines.delay

@Composable
fun LoadScreen(
    mainViewModel: MainViewModel,
    textViewModel: TextViewModel,
    audioViewModel: AudioTalesViewModel,
    onNavigateToHome: () -> Unit
) {
    val fetchAudioSuccess by audioViewModel.fetchSuccess.collectAsState()
    val checkDeviceDataSuccess by mainViewModel.checkDeviceDataSuccess.collectAsState()

    LaunchedEffect(Unit) {
        mainViewModel.checkDeviceData() { exists ->
            if (!exists) {
                Log.d("DeviceCheck", "Устройство НЕ зарегистрировано")
                mainViewModel.registerDeviceData()
            } else {
                Log.d("DeviceCheck", "Устройство зарегистрировано")
            }
        }

        while (!checkDeviceDataSuccess) {
            delay(100)
        }

        audioViewModel.fetchAudioTales()
        textViewModel.fetchTextTales()

        while (!fetchAudioSuccess) {
            delay(100)
        }

        run { onNavigateToHome() }
    }

    val circleSize: Dp = 25.dp
    val circleColor: Color = MaterialTheme.colorScheme.primary
    val spaceBetween: Dp = 10.dp
    val bounceDistance: Dp = 20.dp
    val circles = listOf(
        remember { Animatable(0f) },
        remember { Animatable(0f) },
        remember { Animatable(0f) },
    )
    circles.forEachIndexed { index, animatable ->
        LaunchedEffect(key1 = animatable) {
            delay(index * 100L)
            animatable.animateTo(
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = keyframes {
                        durationMillis = 1200
                        0.0f at 0 using LinearOutSlowInEasing
                        1.0f at 300 using LinearOutSlowInEasing
                        0.0f at 600 using LinearOutSlowInEasing
                        0.0f at 1200 using LinearOutSlowInEasing
                    },
                    repeatMode = RepeatMode.Restart
                )
            )
        }
    }
    val circleValues = circles.map { it.value }
    val distance = with(LocalDensity.current) { bounceDistance.toPx() }
    val lastCircle = circleValues.size - 1

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row {
            circleValues.forEachIndexed { index, value ->
                Box(
                    modifier = Modifier
                        .size(circleSize)
                        .graphicsLayer {
                            translationY = -value * distance
                        }
                        .background(color = circleColor, shape = CircleShape)
                )
                if (index != lastCircle) {
                    Spacer(modifier = Modifier.width(spaceBetween))
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoadPreview() {
    val audioViewModel: AudioTalesViewModel = hiltViewModel()
    val textViewModel: TextViewModel = viewModel()
    val mainViewModel: MainViewModel = hiltViewModel()
    MainProjectTheme(darkTheme = true, dynamicColor = false) {
        LoadScreen(mainViewModel, textViewModel, audioViewModel) {}
    }
}