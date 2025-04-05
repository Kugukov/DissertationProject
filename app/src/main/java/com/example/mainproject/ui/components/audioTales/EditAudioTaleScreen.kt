package com.example.mainproject.ui.components.audioTales

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Environment
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
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
import com.example.mainproject.models.MyViewModelFactory
import com.example.mainproject.ui.components.TopAppBar
import com.example.mainproject.ui.theme.MainProjectTheme
import com.example.mainproject.utils.AudioPlayer
import com.example.mainproject.utils.AudioRecorder
import com.example.mainproject.utils.SaveAlert
import com.example.mainproject.viewmodel.AudioViewModel
import com.example.mainproject.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

@Composable
fun EditAudioTaleScreen(
    audioTaleId: Int,
    title: String,
    description: String,
    fileUrl: String,
    audioViewModel: AudioViewModel,
    navController: NavHostController? = null
) {
    val context = LocalContext.current
    val activity = context as Activity
    val openDialog = remember { mutableStateOf(false) }

    val file = File(
        context.getExternalFilesDir(Environment.DIRECTORY_MUSIC),
        fileUrl.substringAfterLast("\\")
    )
    val editingAudioFile by remember { mutableStateOf(file) }
    var oldAudioFile: File? by remember { mutableStateOf(null) }
    var newAudioFile: File? by remember { mutableStateOf(null) }

    var newTaleTitle by remember { mutableStateOf(title) }
    var newTaleDescription by remember { mutableStateOf(description) }
    val maxTitleLength = 50
    val maxDescriptionLength = 500
    val isErrorTitle = newTaleTitle.length >= maxTitleLength
    val isErrorDescription = newTaleDescription.length >= maxDescriptionLength
    val audioRecorder = remember { AudioRecorder(context) }
    var isRecording by remember { mutableStateOf(false) }
    var buttonText by remember { mutableStateOf("Начать запись") }

    val onSaveButton: () -> Unit = {
        if (newAudioFile != file) {
            file.delete()
        }
        Log.d("Update Audio with audio", "wtf")
        /* TODO при изменении без сказки*/
        newAudioFile?.let {
            Log.d("Update Audio with audio", "$newAudioFile, $newTaleTitle, $newTaleDescription")
            audioViewModel.updateAudioTale(
                taleId = audioTaleId,
                newTitle = newTaleTitle,
                newDescription = newTaleDescription,
                newAudioFile = newAudioFile,
                newAudioDuration = audioViewModel.getAudioDuration(newAudioFile!!)
            )
        } ?: run {
            Log.d("Update Audio without audio", "$newAudioFile, $newTaleTitle, $newTaleDescription")
            audioViewModel.updateAudioTale(
                taleId = audioTaleId,
                newTitle = newTaleTitle,
                newDescription = newTaleDescription,
                newAudioFile = null,
                newAudioDuration = null
            )
        }

        navController?.popBackStack()
        audioViewModel.fetchAudioTales(context)
    }

    val onCancelButton: () -> Unit = {
        navController?.popBackStack()
        openDialog.value = false
    }

    val onDismissButton: () -> Unit = {
        openDialog.value = false
    }

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
                titleText = "Edit Screen",
                doNavigationIcon = {
                    openDialog.value = true
                },
                isOptionEnable = false,
                navController
            )
        },
    ) { padding ->
        Card(
            colors = CardDefaults.cardColors(),
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
                    text = "Изменить сказку:",
                    fontSize = 25.sp,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Left,
                    lineHeight = 30.sp,
                    modifier = Modifier.weight(0.1f)
                )

                Spacer(modifier = Modifier.width(20.dp))

                /*TODO съело текст в placeholoder*/
                OutlinedTextField(
                    value = newTaleTitle,
                    onValueChange = {
                        if (it.length <= maxTitleLength) {
                            newTaleTitle = it
                        }
                    },
                    placeholder = {
                        Text(
                            text = "Название",
                            fontSize = 20.sp
                        )
                    },
                    singleLine = true,
                    isError = isErrorTitle,
                    modifier = Modifier.weight(0.12f)
                )

                Spacer(modifier = Modifier.weight(0.05f))

                OutlinedTextField(
                    value = newTaleDescription,
                    onValueChange = {
                        if (it.length <= maxDescriptionLength) {
                            newTaleDescription = it
                        }
                    },
                    placeholder = {
                        Text(
                            text = "Краткое описание",
                            fontSize = 20.sp
                        )
                    },
                    isError = isErrorDescription,
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

                                if (newTaleTitle.isEmpty()) {
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
                                    buttonText = "Стоп запись"
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
                                    // Удаление предыдущей перезаписи
                                    oldAudioFile?.delete()
                                    // Сохранение новой перезаписи
                                    newAudioFile = audioRecorder.stopRecording()!!
                                    // Определение какую запись удалять при перезаписи
                                    oldAudioFile = newAudioFile
                                    isRecording = false
                                    buttonText = "Пере\nзаписать" // Изменяем текст обратно
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
                        onClick = {
                            if (newAudioFile != null) {
                                AudioPlayer.playAudio(newAudioFile!!, context)
                            } else {
                                if (editingAudioFile.exists()) {
                                    AudioPlayer.playAudio(editingAudioFile, context)
                                } else {
                                    CoroutineScope(Dispatchers.IO).launch {
                                        val downloadedFile =
                                            audioViewModel.downloadAndSaveAudio(
                                                context,
                                                editingAudioFile.name
                                            )
                                        withContext(Dispatchers.Main) {
                                            downloadedFile?.let {
                                                AudioPlayer.playAudio(
                                                    it,
                                                    context
                                                )
                                            }
                                        }
                                    }
                                    Log.d("AudioPlayer", "Ошибка воспроизведения: $file")
                                }

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
                    onClick = {
                        if (newTaleTitle.isNotEmpty() && newTaleDescription.isNotEmpty()) {
                            Log.d("Update Audio ", "$newTaleTitle и $newTaleDescription")
                            onSaveButton()
                        } else {
                            Toast.makeText(
                                context,
                                "Дайте название сказке и дайте описание",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@Button
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EditPreview() {
    val audioViewModel: AudioViewModel = viewModel()
    MainProjectTheme {
        EditAudioTaleScreen(1, "", "", "", audioViewModel)
    }
}