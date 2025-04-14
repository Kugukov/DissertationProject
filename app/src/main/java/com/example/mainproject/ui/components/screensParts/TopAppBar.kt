package com.example.mainproject.ui.components.screensParts

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import com.example.mainproject.utils.ExitAlert

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    titleText: String,
    doNavigationIcon: () -> Unit,
    isOptionEnable: Boolean,
    isToMainScreen: Boolean,
    scrollBehavior: TopAppBarScrollBehavior?,
    navController: NavHostController? = null
) {
    var showExitDialog by remember { mutableStateOf(false) }
    if (showExitDialog) {
        ExitAlert(
            text = "Вы действительно хотите выйти из аккаунта?",
            onExit = {
                showExitDialog = false
                doNavigationIcon()
            },
            onCancelAlert = {
                showExitDialog = false
            }
        )
    }

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = titleText,
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
                if (isToMainScreen) {
                    showExitDialog = true
                } else {
                    navController?.popBackStack()
                }

            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Назад",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }

        },
        actions = {
            if (isOptionEnable) {
                IconButton(
                    onClick = {
                        navController?.navigate("optionScreen")
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

        },
        scrollBehavior = scrollBehavior,
        modifier = Modifier.fillMaxHeight(0.10f)
    )
}