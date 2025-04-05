package com.example.mainproject.ui.components.textTales

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
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
import com.example.mainproject.viewmodel.MainViewModel

@Composable
fun TextScreen(viewModel: MainViewModel, navController: NavHostController? = null) {
    val context = LocalContext.current

    LaunchedEffect(viewModel) {
        viewModel.fetchTextTales(context)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                titleText = "Text Screen",
                doNavigationIcon = {
                    navController?.navigate("homeScreen")
                },
                isOptionEnable = true,
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
                    navController?.navigate("textScreen")
                }
            )
        }
    ) { padding ->
        val cards by viewModel.textTalesList.collectAsState()
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
//                    if (viewModel.isParent.value) {
                    CardItem(
                        taleName = card.title,
                        taleDescription = card.description,
                        cardButtons = { modifier ->
                            val cardId = card.id
                            if (viewModel.isParent.value) {
                                ParentButtons(
                                    "editTextTaleScreen/${cardId}/${card.title}/${card.description}",
                                    {
                                        viewModel.deleteTextTale(context, cardId!!)
                                    },
                                    navController,
                                    modifier.weight(0.25f)
                                )
                            } else {
                                ChildButtons(
                                    Icons.AutoMirrored.Filled.Message,
                                    { },
                                    modifier.weight(0.125f)
                                )
                            }

                            Log.d("CardItemId", card.id.toString())
                        }
                    ) { Log.d("CardItem", "Click") }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TextPreview() {
    val context = LocalContext.current
    val viewModel: MainViewModel = viewModel(factory = MyViewModelFactory(context))
    MainProjectTheme(darkTheme = true, dynamicColor = false) {
        TextScreen(viewModel = viewModel)
    }
}