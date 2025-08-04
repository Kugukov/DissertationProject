package com.example.mainproject.ui.components

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mainproject.R
import com.example.mainproject.ui.theme.MainProjectTheme
import com.example.mainproject.viewmodel.MainViewModel

@Composable
fun HomeScreen(
    mainViewModel: MainViewModel,
    onNavigateToOptions: () -> Unit,
    onNavigateToAudio: () -> Unit
) {

    var isPasswordEnable by remember { mutableStateOf(true) }
    val context = LocalContext.current

    val password by mainViewModel.savedPassword.collectAsState()
    var showPasswordDialog by remember { mutableStateOf(false) }
    val childImage by mainViewModel.savedChildImage
    val parentImage by mainViewModel.savedParentImage

    LaunchedEffect(Unit) {
        mainViewModel.loadImage("saved_child_image.jpg")
        mainViewModel.loadImage("saved_parent_image.jpg")

        if (isPasswordEnable && password == "") {
            showPasswordDialog = true
        }
    }

    if (showPasswordDialog)
        AlertDialog(
            onDismissRequest = { showPasswordDialog = false },
            title = { Text("Установите пароль") },
            text = { Text("Вы ещё не установили родительский пароль!") },
            confirmButton = {
                TextButton(onClick = {
                    onNavigateToOptions()
                    showPasswordDialog = false
                }) {
                    Text("Установить")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPasswordDialog = false }) {
                    Text("Позже")
                }
            }
        )

    BackHandler {
        (context as? ComponentActivity)?.finish()
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(0.70f)
        ) {
            Spacer(modifier = Modifier.weight(0.05f))

            IconButton(
                onClick = {
                    isPasswordEnable = false
                },
                modifier = Modifier
                    .clip(shape = CircleShape)
                    .weight(0.15f)
                    .aspectRatio(1f)
                    .fillMaxSize()
                    .then(
                        if (!isPasswordEnable) Modifier.border(
                            5.dp,
                            MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        ) else Modifier.border(1.dp, MaterialTheme.colorScheme.secondary)
                    )
            ) {
                if (childImage != null) {
                    childImage?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = "Выбранное изображение",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.boy),
                        contentDescription = "Выбранное изображение",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Text(
                text = "Ребенок",
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(0.025f)
            )

            IconButton(
                onClick = {
                    isPasswordEnable = true
                },
                modifier = Modifier
                    .clip(shape = CircleShape)
                    .weight(0.15f)
                    .aspectRatio(1f)
                    .then(
                        if (isPasswordEnable) Modifier.border(
                            5.dp,
                            MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        ) else Modifier.border(1.dp, MaterialTheme.colorScheme.secondary)
                    )
            ) {
                if (parentImage == null) {
                    Image(
                        painter = painterResource(id = R.drawable.parent),
                        contentDescription = "Выбранное изображение",
                        modifier = Modifier.fillMaxSize()
                    )
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

            Text(
                text = "Родитель",
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(0.025f)
            )

        }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .weight(0.13f)
        ) {
            AnimatedVisibility(
                visible = isPasswordEnable,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically(),
            ) {
                OutlinedTextField(
                    value = mainViewModel.enteredPassword.value,
                    label = { Text("Пароль", color = MaterialTheme.colorScheme.onBackground) },
                    onValueChange = { newPassword ->
                        mainViewModel.updateCurrentEnteredPasswordValue(newPassword)
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        cursorColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        focusedTextColor = MaterialTheme.colorScheme.onSurface
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = PasswordVisualTransformation()
                )
            }
        }

        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .weight(0.17f)
                .padding(bottom = 55.dp)
        ) {
            Button(
                onClick = {
                    if (isPasswordEnable) {
                        if (mainViewModel.enteredPassword.value == password) {
                            mainViewModel.parentEntered()
                            onNavigateToAudio()
                        } else {
                            Toast.makeText(context, "Неправильный пароль", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        mainViewModel.childEntered()
                        onNavigateToAudio()
                    }

                },
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .clip(shape = CircleShape)
                    .weight(0.05f)
            ) {
                Text(text = "Sign in",
                    color = MaterialTheme.colorScheme.onPrimary)
            }
        }

    }

}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    val mainViewModel: MainViewModel = hiltViewModel()
    MainProjectTheme(darkTheme = false, dynamicColor = false) {
        HomeScreen(
            mainViewModel = mainViewModel,
            {},
            {})
    }
}