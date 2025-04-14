package com.example.mainproject.utils

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun SaveAlert(
    openDialog: Boolean,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    /* TODO только при наличии данных */
    if (openDialog) {
        AlertDialog(
            onDismissRequest = { onCancel() }, // Закрытие при клике вне диалога
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
                        onCancel()
                    }
                ) {
                    Text("Нет")
                }
            }
        )
    }
}