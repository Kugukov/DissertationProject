package com.example.mainproject.core.ui.alerts

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.mainproject.presentation.theme.MainProjectTheme

@Composable
fun ExitAccountAlert(
    onExit: () -> Unit,
    onCancelAlert: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onCancelAlert() },
        title = {
            Text(text = "Exit")
        },
        text = {
            Text("Do you really want to log out of your account?")
        },
        confirmButton = {
            Button(
                onClick = {
                    onExit()
                }
            ) {
                Text("Yes")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    onCancelAlert()
                }
            ) {
                Text("No")
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