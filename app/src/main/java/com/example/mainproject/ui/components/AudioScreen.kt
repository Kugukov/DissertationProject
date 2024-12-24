package com.example.mainproject.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mainproject.ui.theme.MainProjectTheme
import com.example.mainproject.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioScreen(viewModel: MainViewModel) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Home Screen")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,  // Цвет фона
                    titleContentColor = Color.Black // Цвет заголовка
                ),
                actions = {
                    IconButton(onClick = { /* TODO */ }) {
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
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxHeight(0.25f)
            ) {
                Spacer(modifier = Modifier.weight(0.3f))

                FloatingActionButton(
                    onClick = { /* TODO */ },
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
                    onClick = { /* TODO */ },
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
                    onClick = { /* TODO */ },
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
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .fillMaxHeight(0.65f)
                    .padding(padding)
                    .padding(16.dp)
            ) {
                Text(text = "Welcome to Home Screen!")

                Button(
                    onClick = {
                        viewModel.performAction()
                    },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text(text = "Click me!")
                }

                Text(text = viewModel.resultText)
            }
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AudioPreview() {
    MainProjectTheme {
        AudioScreen(viewModel = MainViewModel())
    }
}