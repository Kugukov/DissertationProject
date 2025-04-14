package com.example.mainproject.ui.components.textTales

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mainproject.ui.components.screensParts.TopAppBar
import com.example.mainproject.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadingScreen(
    title: String,
    content: String,
    mainViewModel: MainViewModel,
    navController: NavHostController? = null
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                titleText = title,
                doNavigationIcon = {
                    navController?.navigate("homeScreen")
                },
                isOptionEnable = mainViewModel.isParent.value,
                false,
                scrollBehavior = scrollBehavior,
                navController
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            item {
                Text(
                    text = content,
                    style = TextStyle(
                        fontSize = 18.sp,
                        lineHeight = 28.sp,
                        textAlign = TextAlign.Justify,
                        fontFamily = FontFamily.Serif
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}