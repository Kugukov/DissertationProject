package com.example.mainproject.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
fun TextScreen(viewModel: MainViewModel, navController: NavHostController? = null) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Text Screen")
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
            )
            {
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
                            contentDescription = "Home"
                        )
                    }

                    Spacer(modifier = Modifier.weight(0.2f))

                    FloatingActionButton(
                        onClick = { navController?.navigate("createTextTaleScreen") },
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
                            tint = Color.Magenta,
                            contentDescription = "Search"
                        )
                    }

                    Spacer(modifier = Modifier.weight(0.3f))
                }
            }
        }
    ) { padding ->
        val cards = viewModel.talesList
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
                    if (viewModel.isParent.value) {
                        CardItem(
                            taleName = card.title.value,
                            taleDescription = card.description.value,
                            cardButtons = { modifier ->
                                ParentButtons(
                                    viewModel,
                                    card.taleId,
                                    card.title.value,
                                    card.description.value,
                                    navController,
                                    modifier.weight(0.25f)
                                )
                                Log.d("CardItemId", card.taleId.toString())
                            }
                        ) { Log.d("CardItem", "Click") }
                    } else {
                        CardItem(
                            taleName = card.title.value,
                            taleDescription = card.description.value,
                            cardButtons = { modifier -> ChildButtons(modifier.weight(0.125f)) }
                        ) { Log.d("CardItem", "Click") }
                    }

                }
            }
        }
    }
}

// Кнопки на сказке для родительской версии
@Composable
fun ParentButtons(
    viewModel: MainViewModel,
    cardId: Int,
    cardTitle: String,
    cardDescription: String,
    navController: NavHostController?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
    ) {
        // Кнопка Редактирования текстовой сказки
        IconButton(
            onClick = {navController?.navigate("editTextTaleScreen/${cardId}") },
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .clip(shape = CircleShape)
                .weight(0.45f)
                .aspectRatio(1f)
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Изменение",
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxSize(),
                tint = Color.White
            )
        }
        Spacer(modifier = Modifier.weight(0.1f))
        // Кнопка Удаления текстовой сказки
        IconButton(
            onClick = { viewModel.deleteOneOfTalesList(cardId) },
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .clip(shape = CircleShape)
                .weight(0.45f)
                .aspectRatio(1f)
        ) {
            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = "Удаление",
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxSize(),
                tint = Color.White
            )
        }
    }

}

@Composable
fun ChildButtons(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
    ) {
        IconButton(
            onClick = {/* TODO */ },
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .clip(shape = CircleShape)
                .aspectRatio(1f)
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Фоновое изображение",
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxSize(),
                tint = Color.White
            )
        }

    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TextPreview() {
    val context = LocalContext.current
    val viewModel: MainViewModel = viewModel(factory = MyViewModelFactory(context))
    MainProjectTheme {
        TextScreen(viewModel = viewModel)
    }
}