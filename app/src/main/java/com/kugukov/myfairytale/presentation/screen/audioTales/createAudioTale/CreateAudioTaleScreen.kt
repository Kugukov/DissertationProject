package com.kugukov.myfairytale.presentation.screen.audioTales.createAudioTale

import android.Manifest
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.kugukov.myfairytale.presentation.component.audioComponents.CreateEditAudioTaleCard
import com.kugukov.myfairytale.presentation.theme.MainProjectTheme
import com.kugukov.myfairytale.core.utils.PermissionsManager
import com.kugukov.myfairytale.presentation.AudioTaleUiEvent
import com.kugukov.myfairytale.core.ui.alerts.DangerTaleCheckAlert
import com.kugukov.myfairytale.core.ui.alerts.SaveWhenDoNavigationIconAlert
import com.kugukov.myfairytale.core.utils.workWithAudio.WAVAudioRecorder
import com.kugukov.myfairytale.presentation.screen.MainViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAudioTaleScreen(
    mainViewModel: MainViewModel,
    createAudioTaleViewModel: CreateAudioTaleViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToOptions: () -> Unit
) {
    val context = LocalContext.current
    val permissionsManager = remember { PermissionsManager(context) }
    val openSaveTaleAlert by mainViewModel.showSaveTaleAlert.collectAsState()
    val openDangerCheckAlert by createAudioTaleViewModel.showDangerAlert.collectAsState()

    val creatingAudioTaleFile by createAudioTaleViewModel.audioFile.collectAsState()
    val taleTitle by createAudioTaleViewModel.title.collectAsState()
    val taleDescription by createAudioTaleViewModel.description.collectAsState()

    val maxTitleLength = 50
    val maxDescriptionLength = 500

    val wavAudioRecorder = remember { WAVAudioRecorder(context) }
    val recordingButtonText by createAudioTaleViewModel.recordingButtonText.collectAsState()

    val isDangerous by createAudioTaleViewModel.isDangerous.collectAsState(false)
    val dangerousWords by createAudioTaleViewModel.dangerousWords.collectAsState(emptyList())

    val requestMicrophonePermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            createAudioTaleViewModel.onMicPermissionResult(isGranted, permissionsManager)
        }

    LaunchedEffect(Unit) {
        createAudioTaleViewModel.eventFlow.collectLatest { event ->
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

    val onSaveButton: () -> Unit = {
        createAudioTaleViewModel.submitNewAudioTale()
    }

    SaveWhenDoNavigationIconAlert(
        openDialog = openSaveTaleAlert,
        onSave = onSaveButton,
        onCancelWithoutSave = {
            mainViewModel.closeSaveTaleAlert { onNavigateBack() }
            createAudioTaleViewModel.deleteTempAudio()
        }
    )

    DangerTaleCheckAlert(
        isDangerous = isDangerous,
        dangerousWords = dangerousWords,
        openDialog = openDangerCheckAlert,
        deleteCurrentAudioTale = {
            createAudioTaleViewModel.deleteTempAudio()
        },
        resetDangerStatus = { createAudioTaleViewModel.resetDangerStatus() },
        onConfirm = {
            createAudioTaleViewModel.closeDangerAlert { onNavigateBack() }
        }
    )

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
                        if (taleTitle.isNotEmpty() || taleDescription.isNotEmpty() || creatingAudioTaleFile != null) {
                            mainViewModel.openSaveTaleAlert()
                        } else {
                            onNavigateBack()
                            createAudioTaleViewModel.deleteTempAudio()
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
            headerName = "Create Audio Tale",
            taleTitle = taleTitle,
            taleDescription = taleDescription,
            onTitleChange = { createAudioTaleViewModel.updateTitle(it) },
            onDescriptionChange = { createAudioTaleViewModel.updateDescription(it) },
            onStartStopRecording = { createAudioTaleViewModel.startStopRecording(recorder = wavAudioRecorder) },
            onPlayClick = {
                creatingAudioTaleFile?.let { createAudioTaleViewModel.playAudio(it) }
            },
            onSaveClick = { onSaveButton() },
            recordingButtonText = recordingButtonText,
            maxTitleLength = maxTitleLength,
            maxDescriptionLength = maxDescriptionLength,
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CreatePreview() {
    val createAudioTaleViewModel: CreateAudioTaleViewModel = hiltViewModel()
    val mainViewModel: MainViewModel = hiltViewModel()
    MainProjectTheme {
        CreateAudioTaleScreen(
            mainViewModel = mainViewModel,
            createAudioTaleViewModel = createAudioTaleViewModel,
            onNavigateBack = {},
            onNavigateToOptions = {}
        )
    }
}