package com.kugukov.myfairytale

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.kugukov.myfairytale.presentation.navigation.AppNavController
import com.kugukov.myfairytale.presentation.theme.MainProjectTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainProjectTheme {
                AppNavController()
            }
        }
    }
}

