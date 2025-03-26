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
import com.example.mainproject.ui.components.audioTales.AudioScreen
import com.example.mainproject.ui.components.textTales.CreateTextTaleScreen
import com.example.mainproject.ui.components.textTales.EditTextTaleScreen
import com.example.mainproject.ui.components.HomeScreen
import com.example.mainproject.ui.components.LoadScreen
import com.example.mainproject.ui.components.OptionScreen
import com.example.mainproject.ui.components.audioTales.CreateAudioTaleScreen
import com.example.mainproject.ui.components.audioTales.EditAudioTaleScreen
import com.example.mainproject.ui.components.textTales.TextScreen
import com.example.mainproject.viewmodel.AudioViewModel
import com.example.mainproject.viewmodel.MainViewModel

@Composable
fun AppNavController(navController: NavHostController = rememberNavController()) {

    val context = LocalContext.current
    val viewModel: MainViewModel = viewModel(factory = MyViewModelFactory(context))
    val audioViewModel: AudioViewModel = viewModel()

    NavHost(navController = navController, startDestination = "loadScreen") {
        composable("loadScreen") { LoadScreen(viewModel, navController) }
        composable("homeScreen") { HomeScreen(viewModel, navController) }
        composable("audioScreen") { AudioScreen(viewModel, audioViewModel, navController) }
        composable("textScreen") { TextScreen(viewModel, navController) }
        composable("optionScreen") { OptionScreen(viewModel, navController) }
        composable("createAudioTaleScreen") { CreateAudioTaleScreen(viewModel, navController) }
        composable("createTextTaleScreen") { CreateTextTaleScreen(viewModel, navController) }
        composable("editTextTaleScreen/{taleId}",
            arguments = listOf(navArgument("taleId") { type = NavType.IntType })
        ) { backStackEntry ->
            val taleId = backStackEntry.arguments?.getInt("taleId") ?: 0
            EditTextTaleScreen(taleId, viewModel, navController) }
        composable("editAudioTaleScreen/{audioTaleId}",
            arguments = listOf(navArgument("audioTaleId") { type = NavType.IntType })
        ) { backStackEntry ->
            val audioTaleId = backStackEntry.arguments?.getInt("audioTaleId") ?: 0
            EditAudioTaleScreen(audioTaleId, audioViewModel, navController) }
    }
}