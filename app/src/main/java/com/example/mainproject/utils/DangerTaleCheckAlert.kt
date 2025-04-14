package com.example.mainproject.utils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mainproject.viewmodel.AudioViewModel

@Composable
fun DangerTaleCheckAlert(
    audioViewModel: AudioViewModel,
    openDialog: Boolean,
    onCloseWithSave: () -> Unit,
    onCloseWithoutSave: () -> Unit,
) {
    val isDangerous by audioViewModel.isDangerous.observeAsState()
    val dangerousWords by audioViewModel.dangerousWords.observeAsState()

    if (openDialog) {
        AlertDialog(
            onDismissRequest = { },
            title = {
                Text(
                    text = "Опасна ли сказка для ребенка?",
                    style = MaterialTheme.typography.titleLarge
                )
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (isDangerous == null) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(top = 16.dp)
                                .fillMaxSize(0.5f)
                        )
                        Text(
                            text = "Проверка",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    } else {
                        if (isDangerous == true) {
                            Text(
                                text = "Сказка не безопасна!",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = "Обнаружены слова:",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = "$dangerousWords",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        } else {
                            Text(
                                text = "Сказка безопасна!",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            },
            confirmButton = {
                if (isDangerous != null) {
                    val onClose: () -> Unit = if (isDangerous == true) {
                        onCloseWithoutSave
                    } else {
                        onCloseWithSave
                    }
                    Button(onClick = {
                        onClose()
                        audioViewModel.updateDangerStatus(null, null)
                    }) {
                        Text("Ок")
                    }
                }
            },
            modifier = Modifier
                .wrapContentSize()
        )
    }
}