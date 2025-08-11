package com.kugukov.myfairytale.presentation.screen.audioTales.editAudioTale

import android.Manifest
import android.os.Environment
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import com.kugukov.myfairytale.presentation.component.audioComponents.CreateEditAudioTaleCard
import com.kugukov.myfairytale.core.utils.PermissionsManager
import com.kugukov.myfairytale.presentation.AudioTaleUiEvent
import com.kugukov.myfairytale.core.ui.alerts.DangerTaleCheckAlert
import com.kugukov.myfairytale.core.ui.alerts.SaveWhenDoNavigationIconAlert
import com.kugukov.myfairytale.core.utils.workWithAudio.WAVAudioRecorder
import com.kugukov.myfairytale.presentation.screen.MainViewModel
import kotlinx.coroutines.flow.collectLatest
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAudioTaleScreen(
    audioTaleId: Int,
    title: String,
    description: String,
    fileUrl: String,
    mainViewModel: MainViewModel,
    editAudioTaleViewModel: EditAudioTaleViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToOptions: () -> Unit
) {
    val context = LocalContext.current
    val permissionsManager = remember { PermissionsManager(context) }
    val openSaveTaleAlert by mainViewModel.showSaveTaleAlert.collectAsState()
    val openDangerCheckAlert by editAudioTaleViewModel.showDangerAlert.collectAsState()

    val file = File(
        context.getExternalFilesDir(Environment.DIRECTORY_MUSIC),
        fileUrl.substringAfterLast("\\")
    )
    val creatingAudioTaleFile by editAudioTaleViewModel.audioFile.collectAsState()

    val taleTitle by editAudioTaleViewModel.title.collectAsState()
    val taleDescription by editAudioTaleViewModel.description.collectAsState()
    val maxTitleLength = 50
    val maxDescriptionLength = 500
    val wavAudioRecorder = remember { WAVAudioRecorder(context) }

    val isDangerous by editAudioTaleViewModel.isDangerous.collectAsState(false)
    val dangerousWords by editAudioTaleViewModel.dangerousWords.collectAsState(emptyList())

    val recordingButtonText by editAudioTaleViewModel.recordingButtonText.collectAsState()

    val onSaveButton: () -> Unit = {
        editAudioTaleViewModel.submitEdit(audioTaleId)
    }

    SaveWhenDoNavigationIconAlert(
        openDialog = openSaveTaleAlert,
        onSave = onSaveButton,
        onCancelWithoutSave = {
            mainViewModel.closeSaveTaleAlert { onNavigateBack() }
            editAudioTaleViewModel.resetState()
        }
    )

    DangerTaleCheckAlert(
        isDangerous = isDangerous,
        dangerousWords = dangerousWords,
        openDialog = openDangerCheckAlert,
        deleteCurrentAudioTale = {
            editAudioTaleViewModel.resetState()
        },
        resetDangerStatus = { editAudioTaleViewModel.resetDangerStatus() },
        onConfirm = {
            editAudioTaleViewModel.closeDangerAlert { onNavigateBack() }
        }
    )

    val requestMicrophonePermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            editAudioTaleViewModel.onMicPermissionResult(isGranted, permissionsManager)
        }

    LaunchedEffect(Unit) {
        editAudioTaleViewModel.init(title, description, file)

        editAudioTaleViewModel.eventFlow.collectLatest { event ->
            when (event) {
                is AudioTaleUiEvent.ShowToast ->
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()

                AudioTaleUiEvent.RequestPermission -> {
                    requestMicrophonePermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                }

                AudioTaleUiEvent.OpenAppSettings -> permissionsManager.openAppSettings()

            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Audio Tale",
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        if (taleTitle.isNotEmpty() && taleDescription.isNotEmpty() || creatingAudioTaleFile != null)
                            mainViewModel.openSaveTaleAlert()
                        else {
                            onNavigateBack()
                            editAudioTaleViewModel.resetState()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = onNavigateToOptions
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Options",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
            )
        },
    ) { padding ->
        CreateEditAudioTaleCard(
            padding = padding,
            headerName = "Edit Audio Tale",
            taleTitle = taleTitle,
            taleDescription = taleDescription,
            onTitleChange = { editAudioTaleViewModel.updateTitle(it) },
            onDescriptionChange = { editAudioTaleViewModel.updateDescription(it) },
            onStartStopRecording = { editAudioTaleViewModel.startStopRecording(recorder = wavAudioRecorder) },
            onPlayClick = {
                creatingAudioTaleFile?.let {
                    editAudioTaleViewModel.checkAudioFileAndPlay(it.name)
                }
            },
            onSaveClick = { onSaveButton() },
            recordingButtonText = recordingButtonText,
            maxTitleLength = maxTitleLength,
            maxDescriptionLength = maxDescriptionLength,
        )
    }
}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun EditPreview() {
//    val audioViewModel: AudioViewModel = viewModel()
//    MainProjectTheme {
//        EditAudioTaleScreen(
//            audioTaleId = 1,
//            title = "",
//            description = "",
//            fileUrl = "",
//            audioViewModel = audioViewModel,
//            onNavigateBack = {},
//            onNavigateToOptions = {}
//        )
//    }
//}