package com.example.mainproject.ui.components.screensParts

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mainproject.utils.AudioPlayer
import java.io.File

@Composable
fun ChildButtons(
    audioFile: File?,
    getAdditionalContent: (() -> Unit)? = null,
    context: Context,
    modifier: Modifier = Modifier
) {
    var isPlaying by remember { mutableStateOf(false) }
    var isPaused by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
    ) {
        // Основная кнопка
        IconButton(
            onClick = {
                when {
                    getAdditionalContent != null -> {
                        getAdditionalContent()
                    }
                    !isPlaying && audioFile != null -> {
                        AudioPlayer.playAudio(audioFile, context)
                        isPlaying = true
                        isPaused = false
                    }

                    isPlaying && !isPaused -> {
                        AudioPlayer.pauseAudio()
                        isPaused = true
                    }

                    isPlaying && isPaused -> {
                        AudioPlayer.resumeAudio()
                        isPaused = false
                    }
                }
            },
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier
                .clip(shape = CircleShape)
                .weight(0.45f)
                .border(2.dp, MaterialTheme.colorScheme.onPrimaryContainer, shape = CircleShape)
                .aspectRatio(1f)
                .shadow(6.dp, shape = CircleShape)
        ) {
            val icon = when {
                getAdditionalContent != null -> Icons.AutoMirrored.Filled.Message
                !isPlaying -> Icons.Default.PlayArrow
                isPaused -> Icons.Default.PlayArrow
                else -> Icons.Default.Pause
            }
            Icon(
                imageVector = icon,
                contentDescription = if (!isPlaying) "Воспроизвести" else "Остановить",
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxSize(),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        // Дополнительная кнопка
        if (getAdditionalContent == null) {
            Spacer(modifier = Modifier.weight(0.1f))
            IconButton(
                onClick = {
                    AudioPlayer.stopAudio()
                    isPlaying = false
                    isPaused = false
                },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                modifier = Modifier
                    .clip(shape = CircleShape)
                    .weight(0.45f)
                    .border(2.dp, MaterialTheme.colorScheme.onPrimaryContainer, shape = CircleShape)
                    .aspectRatio(1f)
                    .shadow(6.dp, shape = CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Stop,
                    contentDescription = "Остановить",
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxSize(),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun ChildButtonsPreview() {
//    MaterialTheme {
//        ChildButtons(
//            imageVector = Icons.Default.PlayArrow,
//            isPlaying = true,
//            getContent = { /* Прочитать */ },
//            getAdditionalContent = { /* Остановить */ },
//            getAlternativeContent = {},
//            modifier = Modifier
//                .padding(16.dp)
//                .fillMaxWidth()
//        )
//    }
//}
