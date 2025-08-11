package com.kugukov.myfairytale.core.ui.alerts

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
                Text(text = "Save the fairy tale?")
            },
            text = {
                Text("Do you want to save a fairy tale before going out?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        onSave()
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        onCancelWithoutSave()
                    }
                ) {
                    Text("No")
                }
            }
        )
    }
}