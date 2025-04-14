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
import com.example.mainproject.ui.components.HomeScreen
import com.example.mainproject.ui.components.LoadScreen
import com.example.mainproject.ui.components.OptionScreen
import com.example.mainproject.ui.components.audioTales.AudioScreen
import com.example.mainproject.ui.components.audioTales.CreateAudioTaleScreen
import com.example.mainproject.ui.components.audioTales.EditAudioTaleScreen
import com.example.mainproject.ui.components.textTales.CreateTextTaleScreen
import com.example.mainproject.ui.components.textTales.EditTextTaleScreen
import com.example.mainproject.ui.components.textTales.ReadingScreen
import com.example.mainproject.ui.components.textTales.TextScreen
import com.example.mainproject.viewmodel.AudioViewModel
import com.example.mainproject.viewmodel.MainViewModel

@Composable
fun AppNavController(navController: NavHostController = rememberNavController()) {

    val context = LocalContext.current
    val mainViewModel: MainViewModel = viewModel(factory = MyViewModelFactory(context))
    val audioViewModel: AudioViewModel = viewModel()

    NavHost(navController = navController, startDestination = "loadScreen") {
        composable("loadScreen") { LoadScreen(mainViewModel, audioViewModel, navController) }
        composable("homeScreen") { HomeScreen(mainViewModel, navController) }
        composable("audioScreen") { AudioScreen(mainViewModel, audioViewModel, navController) }
        composable("textScreen") { TextScreen(mainViewModel, navController) }
        composable("optionScreen") { OptionScreen(mainViewModel, navController) }
        composable(
            "readingScreen/{title}/{content}",
            arguments = listOf(
                navArgument("title") { type = NavType.StringType },
                navArgument("content") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val title = backStackEntry.arguments?.getString("title") ?: ""
            val content = backStackEntry.arguments?.getString("content") ?: ""
            ReadingScreen(title, content, mainViewModel, navController)
        }
        composable("createAudioTaleScreen") {
            CreateAudioTaleScreen(
                mainViewModel,
                audioViewModel,
                navController
            )
        }
        composable("createTextTaleScreen") { CreateTextTaleScreen(mainViewModel, navController) }
        composable(
            "editTextTaleScreen/{taleId}/{title}/{description}",
            arguments = listOf(
                navArgument("taleId") { type = NavType.IntType },
                navArgument("title") { type = NavType.StringType },
                navArgument("description") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val taleId = backStackEntry.arguments?.getInt("taleId") ?: 0
            val title = backStackEntry.arguments?.getString("title") ?: ""
            val description = backStackEntry.arguments?.getString("description") ?: ""
            EditTextTaleScreen(taleId, title, description, mainViewModel, navController)
        }
        composable(
            "editAudioTaleScreen/{audioTaleId}/{title}/{description}/{fileUrl}",
            arguments = listOf(
                navArgument("audioTaleId") { type = NavType.IntType },
                navArgument("title") { type = NavType.StringType },
                navArgument("description") { type = NavType.StringType },
                navArgument("fileUrl") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val audioTaleId = backStackEntry.arguments?.getInt("audioTaleId") ?: 0
            val title = backStackEntry.arguments?.getString("title") ?: ""
            val description = backStackEntry.arguments?.getString("description") ?: ""
            val fileUrl = backStackEntry.arguments?.getString("fileUrl") ?: ""
            EditAudioTaleScreen(
                audioTaleId,
                title,
                description,
                fileUrl,
                mainViewModel,
                audioViewModel,
                navController
            )
        }
    }
}