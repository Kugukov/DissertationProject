package com.example.mainproject.ui.components

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.mainproject.R
import com.example.mainproject.models.MyViewModelFactory
import com.example.mainproject.ui.theme.MainProjectTheme
import com.example.mainproject.viewmodel.MainViewModel

@SuppressLint("AutoboxingStateCreation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateScreen(viewModel: MainViewModel, navController: NavHostController? = null) {
    var newTaleTitle by remember { mutableStateOf("") }
    var newTaleDescription by remember { mutableStateOf("") }
    val isErrorTitle = newTaleTitle.length > 20

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Create Screen")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.Black
                ),
                navigationIcon = {
                    IconButton(onClick = { navController?.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                },
                modifier = Modifier.fillMaxHeight(0.10f)
            )
        },
        ) { padding ->
        Card(
            colors = CardDefaults.cardColors(),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.98f)
                .padding(padding)
                .padding(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .fillMaxHeight()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Новая сказка:",
                    fontSize = 25.sp,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Left,
                    lineHeight = 30.sp,
                    modifier = Modifier.weight(0.1f)
                )

                Spacer(modifier = Modifier.weight(0.05f))

                OutlinedTextField(
                    value = newTaleTitle,
                    onValueChange = { newTaleTitle = it },
                    placeholder = {
                        Text(
                            text = "Название",
                            fontSize = 20.sp
                        )
                    },
                    singleLine = true,
                    isError = isErrorTitle,
                    modifier = Modifier.weight(0.15f)
                )

                Spacer(modifier = Modifier.weight(0.05f))

                OutlinedTextField(
                    value = newTaleDescription,
                    onValueChange = { newTaleDescription = it },
                    placeholder = {
                        Text(
                            text = "Текст сказки",
                            fontSize = 20.sp
                        )
                    },
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.weight(0.05f))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .weight(0.15f),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.weight(0.35f))

                    IconButton(
                        onClick = { navController?.popBackStack() },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier
                            .aspectRatio(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Фоновое изображение",
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxSize(),
                            tint = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.weight(0.05f))

                    Button(
                        modifier = Modifier
                            .fillMaxHeight(),
                        onClick = {
                            viewModel.updateTalesList(viewModel.newTaleId.value, newTaleTitle, newTaleDescription)
                            viewModel.updateNewTaleId()
                            navController?.popBackStack()
                        }
                    ) { Text(text = "Сохранить") }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CreatePreview() {
    val context = LocalContext.current
    val viewModel: MainViewModel = viewModel(factory = MyViewModelFactory(context))
    MainProjectTheme {
        CreateScreen(viewModel = viewModel)
    }
}