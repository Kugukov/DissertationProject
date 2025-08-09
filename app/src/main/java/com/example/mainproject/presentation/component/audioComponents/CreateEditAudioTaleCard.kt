package com.example.mainproject.presentation.component.audioComponents

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CreateEditAudioTaleCard(
    padding: PaddingValues,
    headerName: String,
    taleTitle: String,
    taleDescription: String,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onStartStopRecording: () -> Unit,
    onPlayClick: () -> Unit,
    onSaveClick: () -> Unit,
    recordingButtonText: String,
    maxTitleLength: Int,
    maxDescriptionLength: Int,
    modifier: Modifier = Modifier
) {
    val isErrorTitle = taleTitle.length >= maxTitleLength
    val isErrorDescription = taleDescription.length >= maxDescriptionLength

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.8f)
            .padding(padding)
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxSize()
                .fillMaxHeight()
                .padding(16.dp)
        ) {
            Text(
                text = headerName,
                fontSize = 25.sp,
                color = MaterialTheme.colorScheme.onSurface,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Left,
                lineHeight = 30.sp,
                modifier = modifier.weight(0.1f)
            )

            Spacer(modifier = modifier.width(20.dp))

            OutlinedTextField(
                value = taleTitle,
                onValueChange = {
                    if (it.length <= maxTitleLength) {
                        onTitleChange(it)
                    }
                },
                placeholder = {
                    Text(
                        text = "Name",
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                },
                singleLine = true,
                isError = isErrorTitle,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    errorTextColor = MaterialTheme.colorScheme.error,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    errorBorderColor = MaterialTheme.colorScheme.error
                ),
                modifier = modifier.weight(0.12f)
            )

            Spacer(modifier = modifier.weight(0.05f))

            OutlinedTextField(
                value = taleDescription,
                onValueChange = {
                    if (it.length <= maxDescriptionLength) {
                        onDescriptionChange(it)
                    }
                },
                placeholder = {
                    Text(
                        text = "Short description",
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                },
                isError = isErrorDescription,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    errorTextColor = MaterialTheme.colorScheme.error,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    errorBorderColor = MaterialTheme.colorScheme.error
                ),
                modifier = modifier.weight(0.20f)
            )

            Spacer(modifier = modifier.weight(0.05f))

            Row(
                modifier = modifier
                    .fillMaxWidth(0.98f)
                    .weight(0.15f)
            ) {
                Button(
                    modifier = modifier
                        .weight(0.475f)
                        .fillMaxHeight(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    onClick = {
                        onStartStopRecording()
                    }
                ) {
                    Text(
                        text = recordingButtonText,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = modifier.weight(0.05f))

                Button(
                    modifier = modifier
                        .weight(0.475f)
                        .fillMaxHeight(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    onClick = {
                        onPlayClick()
                    }
                ) {
                    Text(
                        text = "Listen",
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = modifier.weight(0.05f))

            Button(
                modifier = modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .weight(0.15f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                onClick = {
                    onSaveClick()
                }
            ) { Text(text = "Save", fontSize = 20.sp) }
        }
    }
}

@Preview
@Composable
fun CreateEditAudioTaleCardPreview() {
    CreateEditAudioTaleCard(
        padding = PaddingValues(0.dp),
        headerName = "Create New Audio Tale",
        taleTitle = "My Awesome Tale",
        taleDescription = "A short story about a brave knight.",
        onTitleChange = {},
        onDescriptionChange = {},
        onStartStopRecording = {},
        onPlayClick = {},
        onSaveClick = {},
        recordingButtonText = "Start Recording",
        maxTitleLength = 50,
        maxDescriptionLength = 200
    )
}



