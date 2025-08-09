package com.example.mainproject.presentation.screen.option

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.mainproject.R
import com.example.mainproject.presentation.component.ProfileImageDisplay
import com.example.mainproject.presentation.theme.MainProjectTheme
import com.example.mainproject.presentation.screen.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptionScreen(
    mainViewModel: MainViewModel,
    onNavigateBack: () -> Unit
) {
    /* TODO work with forbidden words */

    var passwordCurrentInput by remember { mutableStateOf("") }
    var currentSelection by remember { mutableStateOf("first") }

    val tempChildUriImage by mainViewModel.tempChildUriImage
    val tempParentUriImage by mainViewModel.tempParentUriImage

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        when (currentSelection) {
            "first" -> {
                uri?.let {
                    mainViewModel.setTempChildUriImage(it)
                }
            }

            "second" -> {
                uri?.let {
                    mainViewModel.setTempParentUriImage(it)
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        mainViewModel.loadImage("saved_child_image.jpg")
        mainViewModel.loadImage("saved_parent_image.jpg")
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Options",
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
                        onNavigateBack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }

                },
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
                                text = "New password",
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
                        text = "New pictures:",
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
                            tempChildUriImage?.let {
                                AsyncImage(
                                    model = it,
                                    contentDescription = "Selected child image",
                                    modifier = Modifier.fillMaxSize()
                                )
                            } ?: ProfileImageDisplay(
                                savedImage = mainViewModel.savedChildImage.value,
                                defaultImageRes = R.drawable.boy
                            )
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
                            tempParentUriImage?.let {
                                AsyncImage(
                                    model = it,
                                    contentDescription = "Selected parent image",
                                    modifier = Modifier.fillMaxSize()
                                )
                            } ?: ProfileImageDisplay(
                                savedImage = mainViewModel.savedParentImage.value,
                                defaultImageRes = R.drawable.parent
                            )
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
                                    mainViewModel.setNewPassword(passwordCurrentInput)
                                if (tempChildUriImage != null) {
                                    setAndLoadNewImage(
                                        tempChildUriImage,
                                        "saved_child_image.jpg",
                                        mainViewModel
                                    )
                                }
                                if (tempParentUriImage != null) {
                                    setAndLoadNewImage(
                                        tempParentUriImage,
                                        "saved_parent_image.jpg",
                                        mainViewModel
                                    )
                                }
                                onNavigateBack()
                            }
                        ) { Text(text = "To accept") }
                    }
                }
            }
        }
    )
}

private fun setAndLoadNewImage(
    uri: Uri?,
    fileName: String,
    mainViewModel: MainViewModel
) {
    uri?.let {
        mainViewModel.saveImageToInternalStorage(it, fileName)
        mainViewModel.loadImage(fileName)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun OptionPreview() {
    val mainViewModel: MainViewModel = hiltViewModel()

    MainProjectTheme(darkTheme = true) {
        OptionScreen(mainViewModel = mainViewModel) {}
    }
}