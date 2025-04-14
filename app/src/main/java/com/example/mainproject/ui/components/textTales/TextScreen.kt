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
import com.example.mainproject.utils.ExitAlert
import com.example.mainproject.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextScreen(mainViewModel: MainViewModel, navController: NavHostController? = null) {
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
                titleText = "Text Screen",
                doNavigationIcon = {
                    navController?.popBackStack("homeScreen", false)
                },
                isOptionEnable = mainViewModel.isParent.value,
                true,
                null,
                navController
            )
        },
        bottomBar = {
            BottomAppBar(
                audioButtonColor = MaterialTheme.colorScheme.onSurface,
                textButtonColor = MaterialTheme.colorScheme.secondary,
                doClickMic = {
                    navController?.navigate("audioScreen")
                },
                doClickAdd = {
                    navController?.navigate("createTextTaleScreen")
                },
                doClickText = {
                    mainViewModel.fetchTextTales(context)
                }
            )
        },
        modifier = Modifier.pointerInput(Unit) {
            detectHorizontalDragGestures { _, dragAmount ->
                if (dragAmount > 50) {
                    // Свайп вправо
                    navController?.navigate("audioScreen")
                }
            }
        }
    ) { padding ->
        val cards by mainViewModel.textTalesList.collectAsState()
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
                            if (mainViewModel.isParent.value) {
                                ParentButtons(
                                    "editTextTaleScreen/${cardId}/${card.title}/${card.description}",
                                    { mainViewModel.deleteTextTale(context, cardId!!) },
                                    navController,
                                    modifier.weight(0.25f)
                                )
                            } else {
                                ChildButtons(
                                    audioFile = null,
                                    { navController?.navigate("readingScreen/${card.title}/${card.description}") },
                                    context = context,
                                    modifier.weight(0.125f)
                                )
                            }

                            Log.d("CardItemId", card.id.toString())
                        }
                    ) { navController?.navigate("readingScreen/${card.title}/${card.description}") }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TextPreview() {
    val context = LocalContext.current
    val mainViewModel: MainViewModel = viewModel(factory = MyViewModelFactory(context))
    MainProjectTheme(darkTheme = true, dynamicColor = false) {
        TextScreen(mainViewModel = mainViewModel)
    }
}