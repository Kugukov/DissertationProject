package com.example.mainproject.utils

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mainproject.models.MyViewModelFactory
import com.example.mainproject.ui.components.OptionScreen
import com.example.mainproject.ui.theme.MainProjectTheme
import com.example.mainproject.viewmodel.MainViewModel

@Composable
fun ExitAlert(
    text: String,
    onExit: () -> Unit,
    onCancelAlert: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onCancelAlert() }, // Закрытие при клике вне диалога
        title = {
            Text(text = "Выход")
        },
        text = {
            Text(text)
        },
        confirmButton = {
            Button(
                onClick = {
                    onExit()
                }
            ) {
                Text("Выйти")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    onCancelAlert()
                }
            ) {
                Text("Нет")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ExitAlertPreview() {
    MainProjectTheme {
        ExitAlert(
            text = "TEST",
            onExit = {},
            onCancelAlert = {}
        )
    }
}