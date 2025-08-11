package com.kugukov.myfairytale.presentation.screen.login

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.kugukov.myfairytale.R
import com.kugukov.myfairytale.data.repository.UserProfileRepository
import com.kugukov.myfairytale.data.repository.UserRole
import com.kugukov.myfairytale.presentation.component.roleImage.RoleIconButton
import com.kugukov.myfairytale.presentation.theme.MainProjectTheme

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel,
    onNavigateToOptions: () -> Unit,
    onNavigateToAudio: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val selectedRole by loginViewModel.selectedRole.collectAsState()
    val savedChildUriImage by loginViewModel.savedChildUriImage.collectAsState()
    val savedParentUriImage by loginViewModel.savedParentUriImage.collectAsState()
    val enteredPassword by loginViewModel.enteredPassword.collectAsState()
    val showPasswordDialog by loginViewModel.showPasswordDialog.collectAsState()

    LaunchedEffect(lifecycleOwner) {
        loginViewModel.uiEvent.collect { event ->
            when (event) {
                is LoginViewModel.LoginUiEvent.NavigateToAudio -> {
                    onNavigateToAudio()
                }

                is LoginViewModel.LoginUiEvent.NavigateToOptions -> {
                    onNavigateToOptions()
                }

                LoginViewModel.LoginUiEvent.ExitApp -> {
                    (context as? ComponentActivity)?.finish()
                }

                is LoginViewModel.LoginUiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    BackHandler {
        loginViewModel.exitApp()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 18.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            RoleIconButton(
                isSelected = selectedRole == UserRole.CHILD,
                imageUri = savedChildUriImage,
                defaultImageRes = R.drawable.boy,
                label = "Child",
                onClick = { loginViewModel.selectChild() }
            )

            Spacer(modifier = Modifier.height(16.dp))

            RoleIconButton(
                isSelected = selectedRole == UserRole.PARENT,
                imageUri = savedParentUriImage,
                defaultImageRes = R.drawable.parent,
                label = "Parent",
                onClick = { loginViewModel.selectParent() }
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AnimatedVisibility(
                visible = selectedRole == UserRole.PARENT,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically(),
            ) {
                OutlinedTextField(
                    value = enteredPassword,
                    label = {
                        Text(text = "Password", color = MaterialTheme.colorScheme.onBackground)
                    },
                    onValueChange = { loginViewModel.updateEnteredPassword(it) },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        cursorColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        focusedTextColor = MaterialTheme.colorScheme.onSurface
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { loginViewModel.onLoginClick() },
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .size(64.dp)
                    .clip(shape = CircleShape)
            ) {
                Text(
                    text = "Log in",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }

    if (showPasswordDialog) {
        AlertDialog(
            onDismissRequest = { loginViewModel.dismissPasswordDialog() },
            confirmButton = {
                TextButton(onClick = { loginViewModel.onSetPasswordClick() }) { Text("Set") }
            },
            dismissButton = {
                TextButton(onClick = { loginViewModel.onLaterSetPasswordClick() }) { Text("Later") }
            },
            title = { Text("Set password") },
            text = { Text("You haven't set password yet") },
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    val context = LocalContext.current
    val userProfileRepository = UserProfileRepository(context)
    val loginViewModel = LoginViewModel(userProfileRepository)
    MainProjectTheme {
        LoginScreen(
            loginViewModel = loginViewModel,
            onNavigateToOptions = {},
            onNavigateToAudio = {}
        )
    }
}
