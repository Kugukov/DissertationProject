package com.example.mainproject.ui.components.screensParts

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mainproject.ui.components.CardItem

@Composable
fun ChildButtonsAudioTale(
    isPaused: Boolean,
    isThisPlaying: Boolean,
    onPlayPauseClick: () -> Unit,
    onStopPlayClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val icon = when {
        !isThisPlaying -> Icons.Default.PlayArrow
        isPaused -> Icons.Default.PlayArrow
        else -> Icons.Default.Pause
    }

    Row(
        modifier = modifier
    ) {
        IconButtonForTaleCard(
            imageVector = icon,
            iconContentDescription = if (!isThisPlaying) "Воспроизвести" else "Остановить",
            onClick = { onPlayPauseClick() },
            modifier = modifier.weight(0.45f)
        )

        Spacer(modifier = Modifier.weight(0.1f))

        IconButtonForTaleCard(
            imageVector = Icons.Default.Stop,
            iconContentDescription = "Остановить",
            onClick = { onStopPlayClick() },
            modifier = modifier.weight(0.45f)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ChildButtonsAudioTalePreview() {
    MaterialTheme {
        CardItem(
            taleName = "testTale",
            taleDescription = "testDesc",
            cardButtons = {
                ChildButtonsAudioTale(
                    isPaused = false,
                    isThisPlaying = true,
                    onPlayPauseClick = {},
                    onStopPlayClick = {},
                    modifier = Modifier.size(100.dp)
                )
            },
            doClick = {},
            modifier = Modifier
        )
    }
}
