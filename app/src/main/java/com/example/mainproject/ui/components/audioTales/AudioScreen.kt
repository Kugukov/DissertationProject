package com.example.mainproject.ui.components.audioTales

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.mainproject.models.MyViewModelFactory
import com.example.mainproject.ui.components.screensParts.BottomAppBar
import com.example.mainproject.ui.components.CardItem
import com.example.mainproject.ui.components.screensParts.ChildButtons
import com.example.mainproject.ui.components.screensParts.ParentButtons
import com.example.mainproject.ui.components.screensParts.TopAppBar
import com.example.mainproject.ui.theme.MainProjectTheme
import com.example.mainproject.utils.AudioPlayer
import com.example.mainproject.utils.ExitAlert
import com.example.mainproject.viewmodel.AudioViewModel
import com.example.mainproject.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun AudioScreen(
    mainViewModel: MainViewModel,
    audioViewModel: AudioViewModel,
    navController: NavHostController? = null
) {
    val context = LocalContext.current

    var showExitDialog by remember { mutableStateOf(false) }
    if (showExitDialog)
        ExitAlert(
            text = "Вы действительно хотите выйти из аккаунта?",
            onExit = {
                showExitDialog = false
                navController?.popBackStack("homeScreen", false)
            },
            onCancelAlert = {
                showExitDialog = false
            }
        )

    BackHandler {
        showExitDialog = true
    }

    Scaffold(
        topBar = {
            TopAppBar(
                titleText = "Audio Screen",
                doNavigationIcon = {
                    navController?.navigate("homeScreen")
                },
                isOptionEnable = mainViewModel.isParent.value,
                true,
                null,
                navController
            )
        },
        bottomBar = {
            BottomAppBar(
                audioButtonColor = MaterialTheme.colorScheme.secondary,
                textButtonColor = MaterialTheme.colorScheme.onSurface,
                doClickMic = {
                    audioViewModel.fetchAudioTales(context)
                },
                doClickAdd = {
                    navController?.navigate("createAudioTaleScreen")
                },
                doClickText = {
                    navController?.navigate("textScreen")
                }
            )
        },
        modifier = Modifier.pointerInput(Unit) {
            detectHorizontalDragGestures { _, dragAmount ->
                if (dragAmount < -50) {
                    // Свайп влево
                    navController?.navigate("textScreen")
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
                    items(audioTales) { audioTaleDB ->
                        val fileName =
                            audioTaleDB.fileUrl.substringAfterLast("\\") // Получаем имя файла
                        val file =
                            File(context.getExternalFilesDir(Environment.DIRECTORY_MUSIC), fileName)

                        if (!file.exists()) {
                            CoroutineScope(Dispatchers.IO).launch {
                                audioViewModel.downloadAndSaveAudio(context, fileName)
                            }
                            Log.d("AudioPlayer", "Скачан: $file")
                        }

                        CardItem(
                            taleName = audioTaleDB.title,
                            taleDescription = audioTaleDB.description,
                            cardButtons = { modifier ->
                                val cardId = audioTaleDB.id
                                val encodedTitle = Uri.encode(audioTaleDB.title)
                                val encodedDescription = Uri.encode(audioTaleDB.description)
                                val encodedFileUrl = Uri.encode(audioTaleDB.fileUrl)

                                if (mainViewModel.isParent.value) {
                                    ParentButtons(
                                        route = "editAudioTaleScreen/${cardId}/$encodedTitle/$encodedDescription/$encodedFileUrl",
                                        doDelete = {
                                            audioViewModel.deleteAudioTale(context, cardId)
                                        },
                                        navController,
                                        modifier.weight(0.25f)
                                    )
                                } else {
                                    ChildButtons(
                                        audioFile = file,
                                        null,
                                        context = context,
                                        modifier.weight(0.25f)
                                    )
                                }
                            },
                            doClick = {
                                AudioPlayer.playAudio(file, context)
                            }
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
    val context = LocalContext.current
    val audioViewModel: AudioViewModel = viewModel()
    val mainViewModel: MainViewModel = viewModel(factory = MyViewModelFactory(context))
    MainProjectTheme(darkTheme = true, dynamicColor = false) {
        AudioScreen(mainViewModel, audioViewModel)
    }
}