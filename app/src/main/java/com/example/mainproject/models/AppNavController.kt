package com.example.mainproject.models

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mainproject.ui.components.AudioScreen
import com.example.mainproject.ui.components.HomeScreen
import com.example.mainproject.viewmodel.MainViewModel

@Composable
fun AppNavController(navController: NavHostController = rememberNavController()) {

    NavHost(navController = navController, startDestination = "homeScreen") {
        composable("homeScreen") { HomeScreen(navController) }
        composable("audioScreen") { AudioScreen(MainViewModel()) }
    }
}