package com.example.mainproject.ui.components.textTales

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.mainproject.models.MyViewModelFactory
import com.example.mainproject.ui.theme.MainProjectTheme
import com.example.mainproject.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTextTaleScreen(
    taleId: Int,
    viewModel: MainViewModel,
    navController: NavHostController? = null
) {
    var editingTale = viewModel.getTextTaleById(taleId)
    var newTaleTitle by remember { mutableStateOf(editingTale?.title?.value ?: "") }
    var newTaleDescription by remember { mutableStateOf(editingTale?.description?.value ?: "") }
    val maxTitleLength = 50
    val maxDescriptionLength = 500
    val isErrorTitle = newTaleTitle.length >= maxTitleLength
    val isErrorDescription = newTaleDescription.length >= maxDescriptionLength

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Edit Screen")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.Black
                ),
                navigationIcon = {
                    IconButton(onClick = { navController?.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                },
                modifier = Modifier.fillMaxHeight(0.10f)
            )
        },
    ) { padding ->
        Card(
            colors = CardDefaults.cardColors(),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.98f)
                .padding(padding)
                .padding(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .fillMaxHeight()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Изменить сказку:",
                    fontSize = 25.sp,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Left,
                    lineHeight = 30.sp,
                    modifier = Modifier.weight(0.1f)
                )

                Spacer(modifier = Modifier.weight(0.05f))

                OutlinedTextField(
                    value = newTaleTitle,
                    onValueChange = {
                        if (it.length <= maxTitleLength) {
                            newTaleTitle = it
                        }
                    },
                    singleLine = true,
                    isError = isErrorTitle,
                    modifier = Modifier.weight(0.15f)
                )

                Spacer(modifier = Modifier.weight(0.05f))

                OutlinedTextField(
                    value = newTaleDescription,
                    onValueChange = {
                        if (it.length <= maxTitleLength) {
                            newTaleDescription = it
                        }
                    },
                    isError = isErrorDescription,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.weight(0.05f))

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.1f),
                    onClick = { /* TODO Изменить функцию изменения сказки под БД */
                        editingTale = TextTale(
                            taleId,
                            mutableStateOf(newTaleTitle),
                            mutableStateOf(newTaleDescription)
                        )
                        viewModel.deleteOneOfTextTalesList(taleId)
                        viewModel.addToTextTalesListByTale(editingTale!!)
                        navController?.popBackStack()
                    }
                ) { Text(text = "Сохранить") }

            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EditPreview() {
    val context = LocalContext.current
    val viewModel: MainViewModel = viewModel(factory = MyViewModelFactory(context))
    MainProjectTheme {
        EditTextTaleScreen(1, viewModel = viewModel)
    }
}