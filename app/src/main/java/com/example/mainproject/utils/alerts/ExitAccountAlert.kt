package com.example.mainproject.utils.alerts

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.mainproject.ui.theme.MainProjectTheme

@Composable
fun ExitAccountAlert(
    onExit: () -> Unit,
    onCancelAlert: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onCancelAlert() },
        title = {
            Text(text = "Выход")
        },
        text = {
            Text("Вы действительно хотите выйти из аккаунта?")
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
        ExitAccountAlert(
            onExit = {},
            onCancelAlert = {}
        )
    }
}