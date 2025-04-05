package com.example.mainproject.ui.components.audioTales

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.mainproject.models.MyViewModelFactory
import com.example.mainproject.ui.components.BottomAppBar
import com.example.mainproject.ui.components.CardItem
import com.example.mainproject.ui.components.ChildButtons
import com.example.mainproject.ui.components.ParentButtons
import com.example.mainproject.ui.components.TopAppBar
import com.example.mainproject.ui.theme.MainProjectTheme
import com.example.mainproject.utils.AudioPlayer
import com.example.mainproject.viewmodel.AudioViewModel
import com.example.mainproject.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun AudioScreen(
    mainViewModel: MainViewModel,
    audioViewModel: AudioViewModel,
    navController: NavHostController? = null
) {
    val context = LocalContext.current
    LaunchedEffect(audioViewModel) {
        audioViewModel.fetchAudioTales(context)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                titleText = "Audio Screen",
                doNavigationIcon = {
                    navController?.navigate("homeScreen")
                },
                isOptionEnable = true,
                navController
            )
        },
        bottomBar = {
            BottomAppBar(
                audioButtonColor = MaterialTheme.colorScheme.secondary,
                textButtonColor = MaterialTheme.colorScheme.onSurface,
                doClickMic = {
                    navController?.navigate("audioScreen")
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
                        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_MUSIC), fileName)

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
                                        Icons.Default.PlayArrow,
                                        {
                                            if (file.exists()) {
                                                AudioPlayer.playAudio(file, context)
                                            }
                                        },
                                        modifier.weight(0.125f)
                                    )
                                }
                            },
                            doClick = {
                                Log.d("ПОх", "НИ КИ ТАААААА")
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
    val viewModel: MainViewModel = viewModel(factory = MyViewModelFactory(context))
    MainProjectTheme(darkTheme = true, dynamicColor = false) {
        AudioScreen(viewModel, audioViewModel)
    }
}