package com.example.mainproject.ui.components.audioTales

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mainproject.ui.components.CardItem
import com.example.mainproject.ui.components.screensParts.BottomAppBar
import com.example.mainproject.ui.components.screensParts.ChildButtonsAudioTale
import com.example.mainproject.ui.components.screensParts.ParentButtons
import com.example.mainproject.ui.theme.MainProjectTheme
import com.example.mainproject.utils.alerts.ExitAccountAlert
import com.example.mainproject.viewmodel.AudioTalesViewModel
import com.example.mainproject.viewmodel.MainViewModel
import com.example.mainproject.viewmodel.UserRole

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioTaleScreen(
    mainViewModel: MainViewModel,
    audioViewModel: AudioTalesViewModel,
    onNavigateToHome: () -> Unit,
    onNavigateToOptions: () -> Unit,
    onNavigateToText: () -> Unit,
    onNavigateToCreateAudioTale: () -> Unit,
    onNavigateToEditAudioTale: (taleId: Int, title: String, description: String, fileUrl: String) -> Unit,
) {
    val userRole by mainViewModel.userRole.collectAsState()

    val isPaused by audioViewModel.isPaused.collectAsState()

    val showExitAccountAlert by mainViewModel.showExitAccountAlert.collectAsState()
    if (showExitAccountAlert)
        ExitAccountAlert(
            onExit = {
                mainViewModel.confirmExitAccountAlert(onNavigateToHome)
            },
            onCancelAlert = {
                mainViewModel.closeExitAccountAlert()
            }
        )

    BackHandler {
        mainViewModel.openExitAccountAlert()
    }

    LaunchedEffect(Unit) {
        audioViewModel.fetchAudioTales()
    }


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Аудио-сказки",
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        mainViewModel.openExitAccountAlert()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад",
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
                            contentDescription = "Настройки",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
            )
        },
        bottomBar = {
            BottomAppBar(
                audioButtonColor = MaterialTheme.colorScheme.secondary,
                textButtonColor = MaterialTheme.colorScheme.onSurface,
                doClickMic = {
                    audioViewModel.fetchAudioTales()
                },
                doClickAdd = onNavigateToCreateAudioTale,
                doClickText = onNavigateToText
            )
        },
        modifier = Modifier.pointerInput(Unit) {
            detectHorizontalDragGestures { _, dragAmount ->
                if (dragAmount < -50) {
                    onNavigateToText()
                }
            }
        },
        content = { padding ->
            val audioTales by audioViewModel.audioTalesDB.collectAsState()
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                LazyColumn(
                    contentPadding = PaddingValues(
                        top = padding.calculateTopPadding(),
                        bottom = padding.calculateBottomPadding()
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 40.dp)
                ) {
                    items(items = audioTales, key = { it.id }) { audioTaleDB ->
                        val cardId = audioTaleDB.id
                        CardItem(
                            taleName = audioTaleDB.title,
                            taleDescription = audioTaleDB.description,
                            cardButtons = { modifier ->
                                val encodedTitle = Uri.encode(audioTaleDB.title)
                                val encodedDescription = Uri.encode(audioTaleDB.description)
                                val encodedFileUrl = Uri.encode(audioTaleDB.fileUrl)

                                val isThisPlaying =
                                    audioViewModel.currentlyPlayingCardId.collectAsState().value == cardId

                                when (userRole) {
                                    UserRole.PARENT -> ParentButtons(
                                        onNavigateToEditTale = {
                                            onNavigateToEditAudioTale(
                                                cardId,
                                                encodedTitle,
                                                encodedDescription,
                                                encodedFileUrl
                                            )
                                        },
                                        doDelete = {
                                            audioViewModel.deleteAudioTaleByIdInDB(cardId)
                                        },
                                        modifier.weight(0.25f)
                                    )

                                    UserRole.CHILD ->
                                        ChildButtonsAudioTale(
                                            isPaused = isPaused,
                                            isThisPlaying = isThisPlaying,
                                            onPlayPauseClick = {
                                                audioViewModel.checkAudioFileAndPlay(
                                                    itemId = cardId,
                                                    fileUrl = audioTaleDB.fileUrl
                                                )
                                            },
                                            onStopPlayClick = { audioViewModel.stopAudioTalePlaying() },
                                            modifier = modifier.weight(0.25f)
                                        )
                                }
                            },
                            doClick = {
                                audioViewModel.checkAudioFileAndPlay(
                                    itemId = cardId,
                                    fileUrl = audioTaleDB.fileUrl
                                )
                            },
                            modifier = Modifier
                        )
                    }
                }
            }
        }
    )
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AudioPreview() {
    val audioViewModel: AudioTalesViewModel = hiltViewModel()
    val mainViewModel: MainViewModel = hiltViewModel()
    MainProjectTheme(darkTheme = true, dynamicColor = false) {
        AudioTaleScreen(
            mainViewModel = mainViewModel,
            audioViewModel = audioViewModel,
            onNavigateToHome = {},
            onNavigateToOptions = {},
            onNavigateToText = {},
            onNavigateToCreateAudioTale = {},
            onNavigateToEditAudioTale = { _, _, _, _ -> }
        )
    }
}