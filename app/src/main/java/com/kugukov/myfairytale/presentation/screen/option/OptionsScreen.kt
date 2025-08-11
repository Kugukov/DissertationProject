package com.kugukov.myfairytale.presentation.screen.option

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kugukov.myfairytale.R
import com.kugukov.myfairytale.presentation.component.roleImage.ImagePickerItem
import com.kugukov.myfairytale.presentation.theme.MainProjectTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptionsScreen(
    optionsViewModel: OptionsViewModel,
    onNavigateBack: () -> Unit
) {
    /* TODO work with forbidden words */

    val context = LocalContext.current

    val tempChildUri by optionsViewModel.tempChildUriImage.collectAsState()
    val tempParentUri by optionsViewModel.tempParentUriImage.collectAsState()
    val savedChildUriImage by optionsViewModel.savedChildUriImage.collectAsState()
    val savedParentUriImage by optionsViewModel.savedParentUriImage.collectAsState()
    val enteredPassword by optionsViewModel.enteredPassword.collectAsState()

    LaunchedEffect(Unit) {
        optionsViewModel.uiEvent.collect { event ->
            when (event) {
                is OptionsViewModel.OptionsUiEvent.NavigateBack -> {
                    onNavigateBack()
                }

                is OptionsViewModel.OptionsUiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }

            }
        }
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
                        optionsViewModel.onBackArrowClick()
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
                        value = enteredPassword,
                        label = { Text(text = "New password", fontSize = 20.sp) },
                        onValueChange = { optionsViewModel.updateEnteredPassword(it) },
                        visualTransformation = PasswordVisualTransformation(),
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
                        ImagePickerItem(
                            title = "Child",
                            savedUri = savedChildUriImage,
                            tempUri = tempChildUri,
                            defaultImageRes = R.drawable.boy,
                            onPickImage = { uri -> optionsViewModel.setTempChildUriImage(uri) }
                        )

                        Spacer(modifier = Modifier.weight(0.02f))

                        ImagePickerItem(
                            title = "Parent",
                            savedUri = savedParentUriImage,
                            tempUri = tempParentUri,
                            defaultImageRes = R.drawable.parent,
                            onPickImage = { uri -> optionsViewModel.setTempParentUriImage(uri) }
                        )
                    }

                    Spacer(modifier = Modifier.weight(0.7f))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            modifier = Modifier.fillMaxWidth(0.6f),
                            onClick = {
                                optionsViewModel.applyNewSettings()
                            }
                        ) { Text(text = "To accept") }
                    }
                }
            }
        }
    )
}

@Composable
@Preview
fun OptionsScreenPreview() {
    val optionsViewModel: OptionsViewModel = hiltViewModel()
    MainProjectTheme(darkTheme = true) {
        OptionsScreen(
            optionsViewModel = optionsViewModel,
            onNavigateBack = {}
        )
    }
}