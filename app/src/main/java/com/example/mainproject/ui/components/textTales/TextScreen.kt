package com.example.mainproject.ui.components.textTales

import android.util.Log
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mainproject.ui.components.CardItem
import com.example.mainproject.ui.components.screensParts.BottomAppBar
import com.example.mainproject.ui.components.screensParts.ChildButtonsTextTale
import com.example.mainproject.ui.components.screensParts.ParentButtons
import com.example.mainproject.ui.theme.MainProjectTheme
import com.example.mainproject.utils.alerts.ExitAccountAlert
import com.example.mainproject.viewmodel.MainViewModel
import com.example.mainproject.viewmodel.TextViewModel
import com.example.mainproject.viewmodel.UserRole

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextScreen(
    mainViewModel: MainViewModel,
    textViewModel: TextViewModel,
    onNavigateToHome: () -> Unit,
    onNavigateToOptions: () -> Unit,
    onNavigateToAudio: () -> Unit,
    onNavigateToCreateTextTale: () -> Unit,
    onNavigateToEditTextTale: (taleId: Int, title: String, description: String) -> Unit,
    onNavigateToRead: (title: String, description: String) -> Unit,
) {
    val userRole by mainViewModel.userRole.collectAsState()
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

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Сказки",
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
                    when (userRole) {
                        UserRole.PARENT ->
                            IconButton(
                                onClick = onNavigateToOptions
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = "Настройки",
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }

                        UserRole.CHILD -> {}
                    }
                },
            )
        },
        bottomBar = {
            BottomAppBar(
                audioButtonColor = MaterialTheme.colorScheme.onSurface,
                textButtonColor = MaterialTheme.colorScheme.secondary,
                doClickMic = {
                    onNavigateToAudio()
                },
                doClickAdd = {
                    onNavigateToCreateTextTale()
                },
                doClickText = {
                    textViewModel.fetchTextTales()
                }
            )
        },
        modifier = Modifier.pointerInput(Unit) {
            detectHorizontalDragGestures { _, dragAmount ->
                if (dragAmount > 50) {
                    onNavigateToAudio()
                }
            }
        }
    ) { padding ->
        val cards by textViewModel.textTalesList.collectAsState()
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
                    CardItem(
                        taleName = card.title,
                        taleDescription = card.description,
                        cardButtons = { modifier ->
                            val cardId = card.id
                            when (userRole) {
                                UserRole.PARENT ->
                                    ParentButtons(
                                        onNavigateToEditTale = {
                                            onNavigateToEditTextTale(
                                                cardId!!,
                                                card.title,
                                                card.description
                                            )
                                        },
                                        doDelete = {
                                            textViewModel.deleteTextTale(
                                                cardId!!
                                            )
                                        },
                                        modifier = modifier.weight(0.25f)
                                    )

                                UserRole.CHILD -> {
                                    ChildButtonsTextTale(
                                        getTextContent = {
                                            onNavigateToRead(card.title, card.description)
                                        },
                                        modifier = modifier.weight(0.125f)
                                    )
                                }
                            }
                            Log.d("CardItemId", card.id.toString())
                        },
                        doClick = { onNavigateToRead(card.title, card.description) },
                        modifier = Modifier
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TextPreview() {
    val textViewModel: TextViewModel = viewModel()
    val mainViewModel: MainViewModel = hiltViewModel()
    MainProjectTheme(darkTheme = true, dynamicColor = false) {
        TextScreen(
            mainViewModel = mainViewModel,
            textViewModel = textViewModel,
            onNavigateToHome = {},
            onNavigateToOptions = {},
            onNavigateToAudio = {},
            onNavigateToCreateTextTale = {},
            onNavigateToEditTextTale = { _, _, _ -> },
            onNavigateToRead = { _, _ -> })
    }
}