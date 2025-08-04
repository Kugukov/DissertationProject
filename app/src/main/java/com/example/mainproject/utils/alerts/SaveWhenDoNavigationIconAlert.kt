package com.example.mainproject.utils.alerts

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun SaveWhenDoNavigationIconAlert(
    openDialog: Boolean,
    onSave: () -> Unit,
    onCancelWithoutSave: () -> Unit
) {
    if (openDialog) {
        AlertDialog(
            onDismissRequest = { onCancelWithoutSave() },
            title = {
                Text(text = "Сохранить сказку?")
            },
            text = {
                Text("Вы хотите сохранить сказку перед выходом?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        onSave()
                    }
                ) {
                    Text("Сохранить")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        onCancelWithoutSave()
                    }
                ) {
                    Text("Нет")
                }
            }
        )
    }
}