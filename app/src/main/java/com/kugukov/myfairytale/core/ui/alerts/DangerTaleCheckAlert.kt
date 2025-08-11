package com.kugukov.myfairytale.core.ui.alerts

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DangerTaleCheckAlert(
    openDialog: Boolean,
    isDangerous: Boolean?,
    dangerousWords: List<String>,
    deleteCurrentAudioTale: () -> Unit,
    resetDangerStatus: () -> Unit,
    onConfirm: () -> Unit
) {
    if (openDialog) {
        AlertDialog(
            onDismissRequest = { },
            title = {
                Text(
                    text = "Is a fairy tale dangerous for a child?",
                    style = MaterialTheme.typography.titleLarge
                )
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    when (isDangerous) {
                        null -> {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .padding(top = 16.dp)
                                    .fillMaxSize(0.5f)
                            )
                            Text(
                                text = "Check",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }

                        true -> {
                            Text(
                                text = "The fairy tale is not safe and will be deleted!",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = "Words detected: ",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = dangerousWords.joinToString(", "),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }

                        false -> {
                            Text(
                                text = "The fairy tale is safe!",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (isDangerous == true) {
                            deleteCurrentAudioTale()
                        }
                        onConfirm()
                        resetDangerStatus()
                    },
                    enabled = isDangerous != null
                ) {
                    Text("Ок")
                }
            },
            modifier = Modifier
                .wrapContentSize()
        )
    }
}