package com.example.mainproject.ui.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptionScreen(viewModel: MainViewModel, navController: NavHostController? = null) {
    var passwordCurrentInput by remember { mutableStateOf("") }
    var currentSelection by remember { mutableStateOf("first") }

    val context = LocalContext.current

    var childImageUri by remember { mutableStateOf<Uri?>(null) }
    var parentImageUri by remember { mutableStateOf<Uri?>(null) }
    var childImage by remember { mutableStateOf<Bitmap?>(null) }
    var parentImage by remember { mutableStateOf<Bitmap?>(null) }

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->

        when (currentSelection) {
            "first" -> {
                uri?.let {
                    childImageUri = it
                }
            }

            "second" -> {
                uri?.let {
                    parentImageUri = it
                }
            }
        }

    }

    LaunchedEffect(Unit) {
        viewModel.loadImage(context, "saved_child_image.jpg")
        viewModel.loadImage(context, "saved_parent_image.jpg")
        childImage = viewModel.childImage
        parentImage = viewModel.parentImage
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Настройки")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,  // Цвет фона
                    titleContentColor = Color.Black // Цвет заголовка
                ),
                actions = {

                },
                navigationIcon = {
                    IconButton(onClick = { navController?.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        },
        content = { padding ->
            Card(
                colors = CardDefaults.cardColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f)
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
                    TextField(
                        value = passwordCurrentInput,
                        label = {
                            Text(
                                text = "Новый пароль",
                                fontSize = 20.sp
                            )
                        },
                        onValueChange = { newPassword ->
                            passwordCurrentInput = newPassword
                        },
                        visualTransformation = PasswordVisualTransformation()
                    )

                    Spacer(modifier = Modifier.weight(0.2f))

                    Text(
                        text = "Изменение изображений:",
                        fontSize = 25.sp,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Left,
                        lineHeight = 30.sp
                    )

                    Spacer(modifier = Modifier.weight(0.1f))

                    Row {
                        IconButton(
                            onClick = {
                                currentSelection = "first"
                                photoPicker.launch(
                                    "image/*"
                                )
                            },
                            modifier = Modifier
                                .clip(shape = CircleShape)
                                .weight(0.49f)
                                .aspectRatio(1f)
                                .fillMaxSize()
                        ) {
                            if (childImage == null) {
                                Image(
                                    painter = painterResource(id = R.drawable.boy),
                                    contentDescription = "Выбранное изображение",
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else if (childImageUri != null) {
                                childImageUri?.let {
                                    Image(
                                        painter = rememberAsyncImagePainter(childImageUri),
                                        contentDescription = "Выбранное изображение",
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                            } else {
                                childImage?.let {
                                    Image(
                                        bitmap = it.asImageBitmap(),
                                        contentDescription = "Выбранное изображение",
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                            }


                        }

                        Spacer(modifier = Modifier.weight(0.02f))

                        IconButton(
                            onClick = {
                                currentSelection = "second"
                                photoPicker.launch(
                                    "image/*"
                                )
                            },
                            modifier = Modifier
                                .clip(shape = CircleShape)
                                .weight(0.49f)
                                .aspectRatio(1f)
                        ) {

                            if (parentImage == null) {
                                Image(
                                    painter = painterResource(id = R.drawable.parent),
                                    contentDescription = "Выбранное изображение",
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else if (parentImageUri != null) {
                                parentImageUri?.let {
                                    Image(
                                        painter = rememberAsyncImagePainter(parentImageUri),
                                        contentDescription = "Выбранное изображение",
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                            } else {
                                parentImage?.let {
                                    Image(
                                        bitmap = it.asImageBitmap(),
                                        contentDescription = "Выбранное изображение",
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                            }

                        }
                    }

                    Spacer(modifier = Modifier.weight(0.7f))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            modifier = Modifier.fillMaxWidth(0.6f),
                            onClick = {
                                if (passwordCurrentInput != "")
                                    viewModel.updatePassword(passwordCurrentInput)
                                if (childImageUri != null) {
                                    childImageUri?.let {
                                        viewModel.saveImage(context, it, "saved_child_image.jpg")
                                        childImage = viewModel.childImage
                                    }
                                }
                                if (parentImageUri != null) {
                                    parentImageUri?.let {
                                        viewModel.saveImage(context, it, "saved_parent_image.jpg")
                                        parentImage = viewModel.parentImage
                                    }
                                }


                                navController?.popBackStack()
                            }
                        ) { Text(text = "Принять") }
                    }

                }
            }
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun OptionPreview() {
    val context = LocalContext.current
    val viewModel: MainViewModel = viewModel(factory = MyViewModelFactory(context))

    MainProjectTheme {
        OptionScreen(viewModel = viewModel)
    }
}