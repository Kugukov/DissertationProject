package com.example.mainproject.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mainproject.presentation.screen.home.HomeScreen
import com.example.mainproject.presentation.screen.load.LoadScreen
import com.example.mainproject.presentation.screen.option.OptionScreen
import com.example.mainproject.presentation.screen.audioTales.taleList.AudioTaleScreen
import com.example.mainproject.presentation.screen.audioTales.createAudioTale.CreateAudioTaleScreen
import com.example.mainproject.presentation.screen.audioTales.createAudioTale.CreateAudioTaleViewModel
import com.example.mainproject.presentation.screen.audioTales.editAudioTale.EditAudioTaleScreen
import com.example.mainproject.presentation.screen.audioTales.editAudioTale.EditAudioTaleViewModel
import com.example.mainproject.presentation.screen.textTales.CreateTextTaleScreen
import com.example.mainproject.presentation.screen.textTales.EditTextTaleScreen
import com.example.mainproject.presentation.screen.textTales.ReadingScreen
import com.example.mainproject.presentation.screen.textTales.TextScreen
import com.example.mainproject.presentation.screen.audioTales.taleList.AudioTalesViewModel
import com.example.mainproject.presentation.screen.MainViewModel
import com.example.mainproject.presentation.screen.textTales.TextViewModel

@Composable
fun AppNavController(navController: NavHostController = rememberNavController()) {

    val mainViewModel: MainViewModel = hiltViewModel()
    val audioViewModel: AudioTalesViewModel = hiltViewModel()
    val createAudioTaleViewModel: CreateAudioTaleViewModel = hiltViewModel()
    val editAudioTaleViewModel: EditAudioTaleViewModel = hiltViewModel()
    val textViewModel: TextViewModel = viewModel()

    val onNavigateBack = { navController.popBackStack() }
    val onNavigateToHomeFromAccount = { navController.popBackStack("homeScreen", false) }
    val onNavigateToHomeFromLoadScreen = { navController.navigate("homeScreen") }
    val onNavigateToOptions = { navController.navigate("optionScreen") }

    val onNavigateToText = { navController.navigate("textScreen") }
    val onNavigateToCreateTextTale = { navController.navigate("createTextTaleScreen") }
    val onNavigateToEditTextTale: (Int, String, String) -> Unit = { id, title, description ->
        navController.navigate("editTextTaleScreen/$id/$title/$description")
    }
    val onNavigateToRead: (String, String) -> Unit = { title, description ->
        navController.navigate("readingScreen/$title/$description")
    }

    val onNavigateToAudio = { navController.navigate("audioScreen") }
    val onNavigateToCreateAudioTale = { navController.navigate("createAudioTaleScreen") }
    val onNavigateToEditAudioTale: (Int, String, String, String) -> Unit =
        { id, title, description, fileUrl ->
            navController.navigate("editAudioTaleScreen/$id/$title/$description/$fileUrl")
        }

    NavHost(navController = navController, startDestination = "loadScreen") {
        composable("loadScreen") {
            LoadScreen(
                mainViewModel = mainViewModel,
                onNavigateToHome = { onNavigateToHomeFromLoadScreen() }
            )
        }
        composable("homeScreen") {
            HomeScreen(
                mainViewModel = mainViewModel,
                onNavigateToOptions = onNavigateToOptions,
                onNavigateToAudio = onNavigateToAudio
            )
        }
        composable("audioScreen") {
            AudioTaleScreen(
                mainViewModel = mainViewModel,
                audioViewModel = audioViewModel,
                onNavigateToHome = { onNavigateToHomeFromAccount() },
                onNavigateToText = onNavigateToText,
                onNavigateToOptions = onNavigateToOptions,
                onNavigateToCreateAudioTale = onNavigateToCreateAudioTale,
                onNavigateToEditAudioTale = onNavigateToEditAudioTale,
            )
        }
        composable("textScreen") {
            TextScreen(
                mainViewModel = mainViewModel,
                textViewModel = textViewModel,
                onNavigateToHome = { onNavigateToHomeFromAccount() },
                onNavigateToOptions = onNavigateToOptions,
                onNavigateToAudio = onNavigateToAudio,
                onNavigateToCreateTextTale = onNavigateToCreateTextTale,
                onNavigateToEditTextTale = onNavigateToEditTextTale,
                onNavigateToRead = onNavigateToRead
            )
        }
        composable("optionScreen") {
            OptionScreen(
                mainViewModel = mainViewModel,
                onNavigateBack = { onNavigateBack() }
            )
        }
        composable(
            "readingScreen/{title}/{content}",
            arguments = listOf(
                navArgument("title") { type = NavType.StringType },
                navArgument("content") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val title = backStackEntry.arguments?.getString("title") ?: ""
            val content = backStackEntry.arguments?.getString("content") ?: ""
            ReadingScreen(
                title = title,
                content = content,
                onNavigateBack = { onNavigateBack() },
            )
        }
        composable("createAudioTaleScreen") {
            CreateAudioTaleScreen(
                mainViewModel = mainViewModel,
                createAudioTaleViewModel = createAudioTaleViewModel,
                onNavigateBack = { onNavigateBack() },
                onNavigateToOptions = onNavigateToOptions
            )
        }
        composable("createTextTaleScreen") {
            CreateTextTaleScreen(
                mainViewModel = mainViewModel,
                textViewModel = textViewModel,
                onNavigateBack = { onNavigateBack() },
                onNavigateToOptions = onNavigateToOptions
            )
        }
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
            EditTextTaleScreen(
                taleId = taleId,
                title = title,
                description = description,
                mainViewModel = mainViewModel,
                textViewModel = textViewModel,
                onNavigateBack = { onNavigateBack() },
                onNavigateToOptions = onNavigateToOptions
            )
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
                audioTaleId = audioTaleId,
                title = title,
                description = description,
                fileUrl = fileUrl,
                mainViewModel = mainViewModel,
                editAudioTaleViewModel = editAudioTaleViewModel,
                onNavigateBack = { onNavigateBack() },
                onNavigateToOptions = onNavigateToOptions
            )
        }
    }
}