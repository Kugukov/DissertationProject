package com.example.mainproject.ui.components.audioTales

import android.media.MediaPlayer
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.mainproject.models.MyViewModelFactory
import com.example.mainproject.ui.components.CardItem
import com.example.mainproject.ui.components.ChildButtons
import com.example.mainproject.ui.components.ParentButtons
import com.example.mainproject.ui.theme.MainProjectTheme
import com.example.mainproject.viewmodel.AudioViewModel
import com.example.mainproject.viewmodel.MainViewModel
import java.io.File
import java.io.IOException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioScreen(
    mainViewModel: MainViewModel,
    audioViewModel: AudioViewModel,
    navController: NavHostController? = null
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Audio Screen")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.Black
                ),
                actions = {
                    IconButton(onClick = { navController?.navigate("optionScreen") }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings"
                        )
                    }
                },
                modifier = Modifier.fillMaxHeight(0.10f)
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.25f)
                    .background(Color.White.copy(alpha = 0.6f))
            ) {
                BottomAppBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    containerColor = Color.Transparent,
                    tonalElevation = 0.dp,
                ) {
                    Spacer(modifier = Modifier.weight(0.3f))

                    FloatingActionButton(
                        onClick = { navController?.navigate("audioScreen") },
                        shape = CircleShape,
                        modifier = Modifier.padding(top = 30.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            tint = Color.Magenta,
                            contentDescription = "Home"
                        )
                    }

                    Spacer(modifier = Modifier.weight(0.2f))

                    FloatingActionButton(
                        onClick = { navController?.navigate("createAudioTaleScreen") },
                        shape = CircleShape,
                        modifier = Modifier
                            .padding(bottom = 30.dp)
                            .size(85.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add",
                            modifier = Modifier.size(50.dp)
                        )
                    }

                    Spacer(modifier = Modifier.weight(0.2f))

                    FloatingActionButton(
                        onClick = { navController?.navigate("textScreen") },
                        shape = CircleShape,
                        modifier = Modifier.padding(top = 30.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.TextFields,
                            contentDescription = "Search"
                        )
                    }

                    Spacer(modifier = Modifier.weight(0.3f))
                }
            }

        },
        content = { padding ->
            audioViewModel.loadAudioFiles(LocalContext.current)
            val cards = audioViewModel.audioRecords
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
                    items(cards) { card ->
                        if (mainViewModel.isParent.value) {
                            CardItem(
                                taleName = card.title.value,
                                taleDescription = card.description.value,
                                cardButtons = { modifier ->
                                    val cardId = card.audioTaleId
                                    ParentButtons(
                                        route = "editAudioTaleScreen/${cardId}",
                                        doDelete = { audioViewModel.removeAudioRecord(card) },
                                        navController,
                                        modifier.weight(0.25f)
                                    )
                                },
                                doClick = { }
                            )
                        } else {
                            var isPlaying by remember { mutableStateOf(false) }
                            val mediaPlayer = remember { MediaPlayer() }
                            CardItem(
                                taleName = card.title.value,
                                taleDescription = card.description.value,
                                cardButtons = { modifier ->
                                    val cardId = card.audioTaleId

                                    ChildButtons(
                                        route = "",
                                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                        // Определение запущено ли воспроизведение
                                        getContent = {
                                            if (isPlaying) {
                                                mediaPlayer.stop()
                                                mediaPlayer.reset()
                                                isPlaying = false
                                            } else {
                                                try {
                                                    mediaPlayer.setDataSource(card.audioFile.absolutePath)
                                                    mediaPlayer.prepare()
                                                    mediaPlayer.start()
                                                    isPlaying = true

                                                    // Останавливаем воспроизведение и меняем иконку после завершения
                                                    mediaPlayer.setOnCompletionListener {
                                                        isPlaying = false
                                                        mediaPlayer.reset()
                                                    }
                                                } catch (e: IOException) {
                                                    e.printStackTrace()
                                                }
                                            }
                                        },
                                        navController,
                                        modifier.weight(0.125f)
                                    )
                                },
                                doClick = {
                                    if (isPlaying) {
                                        mediaPlayer.stop()
                                        mediaPlayer.reset()
                                        isPlaying = false
                                    } else {
                                        try {
                                            mediaPlayer.setDataSource(card.audioFile.absolutePath)
                                            mediaPlayer.prepare()
                                            mediaPlayer.start()
                                            isPlaying = true

                                            // Останавливаем воспроизведение и меняем иконку после завершения
                                            mediaPlayer.setOnCompletionListener {
                                                isPlaying = false
                                                mediaPlayer.reset()
                                            }
                                        } catch (e: IOException) {
                                            e.printStackTrace()
                                        }
                                    }
                                }
                            )
                        }
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
    MainProjectTheme {
        AudioScreen(viewModel, audioViewModel)
    }
}