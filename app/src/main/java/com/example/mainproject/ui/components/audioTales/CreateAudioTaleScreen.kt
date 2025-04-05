package com.example.mainproject.ui.components.audioTales

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.mainproject.ui.components.TopAppBar
import com.example.mainproject.ui.theme.MainProjectTheme
import com.example.mainproject.utils.AudioPlayer
import com.example.mainproject.utils.AudioRecorder
import com.example.mainproject.utils.SaveAlert
import com.example.mainproject.viewmodel.AudioViewModel
import java.io.File

@Composable
fun CreateAudioTaleScreen(
    audioViewModel: AudioViewModel,
    navController: NavHostController? = null
) {
    val context = LocalContext.current
    val openDialog = remember { mutableStateOf(false) }
    val activity = context as Activity
    var creatingTale by remember { mutableStateOf<File?>(null) }
    var taleTitle by remember { mutableStateOf("") }
    var taleDescription by remember { mutableStateOf("") }
    val maxTitleLength = 50
    val maxDescriptionLength = 500
    val isErrorTitle = taleTitle.length >= maxTitleLength
    val isErrorDescription = taleDescription.length >= maxDescriptionLength
    val audioRecorder = remember { AudioRecorder(context) }
    var isRecording by remember { mutableStateOf(false) }
    var buttonText by remember { mutableStateOf("Начать запись") }

    val onSaveButton: () -> Unit = {
        val uploadTale = creatingTale?.let {
            AudioTale(
                audioFile = it,
                title = taleTitle,
                description = taleDescription,
                audioDuration = audioViewModel.getAudioDuration(it)
            )
        }
        uploadTale?.let { tale ->
            audioViewModel.uploadAudioData(context, tale)
        }

        audioViewModel.fetchAudioTales(context)
        navController?.popBackStack()
    }

    val onCancelButton: () -> Unit = {
        navController?.popBackStack()
        openDialog.value = false
    }

    val onDismissButton: () -> Unit = {
        openDialog.value = false
    }

    // Запрос на доступ к микрофону
    val requestPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(
                        activity,
                        Manifest.permission.RECORD_AUDIO
                    )
                ) {
                    // Пользователь нажал "Не спрашивать снова"
                    Toast.makeText(
                        context,
                        "Перейдите в настройки и включите доступ к микрофону",
                        Toast.LENGTH_LONG
                    ).show()
                    openAppSettings(context)
                } else {
                    Toast.makeText(context, "Разрешение не предоставлено!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

    Scaffold(
        topBar = {
            TopAppBar(
                titleText = "Create Screen",
                doNavigationIcon = {
                    openDialog.value = true
                },
                isOptionEnable = false,
                navController
            )
        },
    ) { padding ->

        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.75f)
                .padding(padding)
                .padding(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .fillMaxHeight()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Новая сказка:",
                    fontSize = 25.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Left,
                    lineHeight = 30.sp,
                    modifier = Modifier.weight(0.1f)
                )

                Spacer(modifier = Modifier.width(20.dp))

                OutlinedTextField(
                    value = taleTitle,
                    onValueChange = {
                        if (it.length <= maxTitleLength) {
                            taleTitle = it
                        }
                    },
                    placeholder = {
                        Text(
                            text = "Название",
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
                    modifier = Modifier.weight(0.12f)
                )

                Spacer(modifier = Modifier.weight(0.05f))

                OutlinedTextField(
                    value = taleDescription,
                    onValueChange = {
                        if (it.length <= maxDescriptionLength) {
                            taleDescription = it
                        }
                    },
                    placeholder = {
                        Text(
                            text = "Краткое описание",
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
                    modifier = Modifier.weight(0.20f)
                )

                Spacer(modifier = Modifier.weight(0.05f))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.98f)
                        .weight(0.15f)
                ) {
                    Button(
                        modifier = Modifier
                            .weight(0.475f)
                            .fillMaxHeight(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        onClick = {
                            if (!isRecording) {
                                // Начинало записи
                                if (ContextCompat.checkSelfPermission(
                                        context,
                                        Manifest.permission.RECORD_AUDIO
                                    ) != PackageManager.PERMISSION_GRANTED
                                ) {
                                    requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                    return@Button
                                }

                                if (taleTitle.isEmpty()) {
                                    Toast.makeText(
                                        context,
                                        "Дайте название сказке",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    return@Button
                                }

                                try {
                                    audioRecorder.startRecording()
                                    isRecording = true
                                    buttonText = "Закончить запись"
                                } catch (e: SecurityException) {
                                    Toast.makeText(
                                        context,
                                        "Нет доступа к микрофону",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                // Остановка записи
                                try {
                                    creatingTale = audioRecorder.stopRecording()
                                    isRecording = false
                                    buttonText = "Начать запись" // Изменяем текст обратно
                                } catch (e: RuntimeException) {
                                    Log.e("AudioRecorder", "Ошибка остановки записи: ${e.message}")
                                }
                            }
                        }
                    ) {
                        Text(
                            text = buttonText,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.weight(0.05f))

                    Button(
                        modifier = Modifier
                            .weight(0.475f)
                            .fillMaxHeight(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        onClick = {
                            creatingTale?.let {
                                AudioPlayer.playAudio(creatingTale!!, context)
                            }
                        }
                    ) {
                        Text(
                            text = "Слушать",
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(0.05f))

                Button(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                        .weight(0.15f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    onClick = {
                        if (creatingTale != null) {
                            if (taleTitle.isNotEmpty() && taleDescription.isNotEmpty()) {
                                onSaveButton()
                            } else {
                                Toast.makeText(
                                    context,
                                    "Дайте название сказке и дайте описание",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@Button
                            }

                        } else {
                            Toast.makeText(
                                context,
                                "Запишите сказку",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    }
                ) { Text(text = "Сохранить", fontSize = 20.sp) }
            }
        }
    }
    SaveAlert (
        openDialog = openDialog.value,
        onSave = onSaveButton,
        onCancel = onCancelButton,
        onDismiss = onDismissButton
    )
}

/**
 * Функция для открытия настроек приложения, если пользователь запретил разрешение "Не спрашивать снова"
 */
fun openAppSettings(context: Context) {
    val intent = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", context.packageName, null)
    )
    context.startActivity(intent)
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CreatePreview() {
    val audioViewModel: AudioViewModel = viewModel()
    MainProjectTheme {
        CreateAudioTaleScreen(audioViewModel = audioViewModel)
    }
}