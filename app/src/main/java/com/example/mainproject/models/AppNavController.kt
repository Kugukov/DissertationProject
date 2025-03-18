package com.example.mainproject.models

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mainproject.ui.components.AudioScreen
import com.example.mainproject.ui.components.CreateTextTaleScreen
import com.example.mainproject.ui.components.EditTextTaleScreen
import com.example.mainproject.ui.components.HomeScreen
import com.example.mainproject.ui.components.LoadScreen
import com.example.mainproject.ui.components.OptionScreen
import com.example.mainproject.ui.components.TextScreen
import com.example.mainproject.viewmodel.MainViewModel

@Composable
fun AppNavController(navController: NavHostController = rememberNavController()) {

    val context = LocalContext.current
    val viewModel: MainViewModel = viewModel(factory = MyViewModelFactory(context))

    NavHost(navController = navController, startDestination = "textScreen") {
        composable("loadScreen") { LoadScreen(navController) }
        composable("homeScreen") { HomeScreen(viewModel, navController) }
        composable("audioScreen") { AudioScreen(viewModel, navController) }
        composable("textScreen") { TextScreen(viewModel, navController) }
        composable("optionScreen") { OptionScreen(viewModel, navController) }
        composable("createTextTaleScreen") { CreateTextTaleScreen(viewModel, navController) }
        composable("editTextTaleScreen/{taleId}",
            arguments = listOf(navArgument("taleId") { type = NavType.IntType })
        ) { backStackEntry ->
            val taleId = backStackEntry.arguments?.getInt("taleId") ?: 0
            EditTextTaleScreen(taleId, viewModel, navController) }
    }
}