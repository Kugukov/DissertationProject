package com.example.mainproject.presentation.screen.textTales

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mainproject.presentation.theme.MainProjectTheme
import com.example.mainproject.core.ui.alerts.SaveWhenDoNavigationIconAlert
import com.example.mainproject.presentation.screen.MainViewModel
import com.example.mainproject.presentation.screen.UserRole

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTextTaleScreen(
    taleId: Int,
    title: String,
    description: String,
    mainViewModel: MainViewModel,
    textViewModel: TextViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToOptions: () -> Unit,
) {
    val context = LocalContext.current
    val userRole by mainViewModel.userRole.collectAsState()
    val openNavigationIconDialog = remember { mutableStateOf(false) }
    var newTaleTitle by remember { mutableStateOf(title) }
    var newTaleDescription by remember { mutableStateOf(description) }
    val maxTitleLength = 50
    val maxDescriptionLength = 500
    val isErrorTitle = newTaleTitle.length >= maxTitleLength
    val isErrorDescription = newTaleDescription.length >= maxDescriptionLength

    val onSaveButton: () -> Unit = {
        if (newTaleTitle.isNotEmpty() && newTaleDescription.isNotEmpty()) {
            textViewModel.updateTextTale(
                taleId,
                newTaleTitle,
                newTaleDescription
            )
            onNavigateBack()
        } else {
            Toast.makeText(
                context,
                "Give title and write your fairy tale",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    SaveWhenDoNavigationIconAlert(
        openDialog = openNavigationIconDialog.value,
        onSave = onSaveButton,
        onCancelWithoutSave = {
            openNavigationIconDialog.value = false
            onNavigateBack()
        }
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Text Tale",
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
                        if (newTaleTitle.isNotEmpty() && newTaleDescription.isNotEmpty()) {
                            openNavigationIconDialog.value = true
                        } else {
                            onNavigateBack()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
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
                                    contentDescription = "Options",
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }

                        UserRole.CHILD -> { }
                    }
                },
            )
        },
    ) { padding ->
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
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
                    text = "Edit fairy tale:",
                    fontSize = 25.sp,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Left,
                    lineHeight = 30.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(0.1f)
                )

                Spacer(modifier = Modifier.weight(0.05f))

                OutlinedTextField(
                    value = newTaleTitle,
                    onValueChange = {
                        if (it.length <= maxTitleLength) {
                            newTaleTitle = it
                        }
                    },
                    placeholder = {
                        Text(
                            text = "Title",
                            fontSize = 20.sp
                        )
                    },
                    singleLine = true,
                    isError = isErrorTitle,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        errorTextColor = MaterialTheme.colorScheme.error,

                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        errorBorderColor = MaterialTheme.colorScheme.error,

                        cursorColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.weight(0.15f)
                )

                Spacer(modifier = Modifier.weight(0.05f))

                OutlinedTextField(
                    value = newTaleDescription,
                    onValueChange = {
                        if (it.length <= maxTitleLength) {
                            newTaleDescription = it
                        }
                    },
                    placeholder = {
                        Text(
                            text = "Fairy tale: ",
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    },
                    isError = isErrorDescription,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        errorTextColor = MaterialTheme.colorScheme.error,

                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        errorBorderColor = MaterialTheme.colorScheme.error,

                        cursorColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.weight(0.05f))

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    onClick = {
                        onSaveButton()
                    }
                ) { Text(text = "Save", fontSize = 20.sp) }

            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EditPreview() {
    val textViewModel: TextViewModel = viewModel()
    val mainViewModel: MainViewModel = hiltViewModel()
    MainProjectTheme {
        EditTextTaleScreen(
            taleId = 1,
            title = "",
            description = "",
            mainViewModel = mainViewModel,
            textViewModel = textViewModel,
            onNavigateBack = {},
            onNavigateToOptions = {})
    }
}