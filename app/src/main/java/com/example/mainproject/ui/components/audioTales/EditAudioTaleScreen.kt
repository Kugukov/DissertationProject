package com.example.mainproject.ui.components.audioTales

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.view.MotionEvent
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.example.mainproject.utils.AudioRecorder
import com.example.mainproject.viewmodel.AudioViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun EditAudioTaleScreen(
    audioTaleId: Int,
    audioViewModel: AudioViewModel,
    navController: NavHostController? = null
) {
    val context = LocalContext.current
    val activity = context as Activity
    val editingTale = audioViewModel.audioRecords.find { it.audioTaleId == audioTaleId } ?: return
    var newAudioFile by remember { mutableStateOf(editingTale.audioFile) }
    var newTaleTitle by remember { mutableStateOf(editingTale.title.value) }
    var newTaleDescription by remember { mutableStateOf(editingTale.description.value) }
    val maxTitleLength = 50
    val maxDescriptionLength = 500
    val isErrorTitle = newTaleTitle.length >= maxTitleLength
    val isErrorDescription = newTaleDescription.length >= maxDescriptionLength
    val audioRecorder = remember { AudioRecorder(context) }

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
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Edit Screen")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.Black
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        /* TODO сохранение при выходе */
                        navController?.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                },
                modifier = Modifier.fillMaxHeight(0.10f)
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
                    modifier = Modifier.weight(0.10f)
                )

                Spacer(modifier = Modifier.weight(0.05f))

                OutlinedTextField(
                    value = newTaleDescription,
                    onValueChange = {
                        if (it.length <= maxTitleLength) {
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
                            .fillMaxHeight()
                            .pointerInteropFilter { motionEvent ->
                                when (motionEvent.action) {
                                    MotionEvent.ACTION_DOWN -> {
                                        if (ContextCompat.checkSelfPermission(
                                                context,
                                                Manifest.permission.RECORD_AUDIO
                                            ) != PackageManager.PERMISSION_GRANTED
                                        ) {
                                            requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                            return@pointerInteropFilter true
                                        }
                                        if (newTaleTitle.isEmpty()) {
                                            Toast
                                                .makeText(
                                                    context,
                                                    "Дайте название сказке",
                                                    Toast.LENGTH_SHORT
                                                )
                                                .show()
                                            return@pointerInteropFilter true
                                        }
                                        val fileName = newTaleTitle.ifEmpty {
                                            "audio_${System.currentTimeMillis()}"
                                        }

                                        try {
                                            audioRecorder.startRecording(fileName)
                                        } catch (e: SecurityException) {
                                            Toast
                                                .makeText(
                                                    context,
                                                    "Нет доступа к микрофону",
                                                    Toast.LENGTH_SHORT
                                                )
                                                .show()
                                        }
                                        true
                                    }

                                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                                        newAudioFile = audioRecorder.stopRecording()!!
                                        true
                                    }

                                    else -> false
                                }
                            },
                        onClick = { }
                    ) {
                        Text(
                            text = "Пере\nзаписать",
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
                            MediaPlayer().apply {
                                setDataSource(newAudioFile.absolutePath)
                                prepare()
                                start()
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
                        editingTale.audioFile.delete()
                        audioViewModel.updateAudioRecord(
                            editingTale.copy(
                                title = mutableStateOf(newTaleTitle),
                                description = mutableStateOf(newTaleDescription),
                                audioFile = newAudioFile,
                            )
                        )
                        navController?.popBackStack()
                    }
                ) { Text(text = "Сохранить", fontSize = 20.sp) }
            }
        }
    }
}