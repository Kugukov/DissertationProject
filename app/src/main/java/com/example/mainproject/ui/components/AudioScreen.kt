package com.example.mainproject.ui.components

import android.util.Log
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Mic
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.mainproject.models.MyViewModelFactory
import com.example.mainproject.ui.theme.MainProjectTheme
import com.example.mainproject.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioScreen(viewModel: MainViewModel, navController: NavHostController? = null) {
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
                        onClick = { /* TODO */ },
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
                        onClick = { navController?.navigate("textScreen") },
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
            val cards = List(10) { "Элемент 1" }
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
//                    items(cards) { card ->
//                        if (viewModel.isParent.value) {
//                            CardItem(
//                                taleName = card,
//                                taleDescription = card,
//                                cardButtons = { modifier -> ParentButtons(modifier.weight(0.25f)) }
//                            ) { Log.d("CardItem", "Click") }
//                        } else {
//                            CardItem(
//                                taleName = card,
//                                taleDescription = card,
//                                cardButtons = { modifier -> ChildButtons(modifier.weight(0.125f)) }
//                            ) { Log.d("CardItem", "Click") }
//                        }
//
//                    }
                }
            }
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AudioPreview() {
    val context = LocalContext.current
    val viewModel: MainViewModel = viewModel(factory = MyViewModelFactory(context))

    MainProjectTheme {
        AudioScreen(viewModel = viewModel)
    }
}