package com.example.mainproject.ui.components.textTales

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.mainproject.models.MyViewModelFactory
import com.example.mainproject.ui.components.screensParts.TopAppBar
import com.example.mainproject.ui.theme.MainProjectTheme
import com.example.mainproject.utils.SaveAlert
import com.example.mainproject.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTextTaleScreen(
    taleId: Int,
    title: String,
    description: String,
    mainViewModel: MainViewModel,
    navController: NavHostController? = null
) {
    val context = LocalContext.current
    val openDialog = remember { mutableStateOf(false) }
    var newTaleTitle by remember { mutableStateOf(title) }
    var newTaleDescription by remember { mutableStateOf(description) }
    val maxTitleLength = 50
    val maxDescriptionLength = 500
    val isErrorTitle = newTaleTitle.length >= maxTitleLength
    val isErrorDescription = newTaleDescription.length >= maxDescriptionLength

    val onSaveButton: () -> Unit = {
        if (newTaleTitle.isNotEmpty() && newTaleDescription.isNotEmpty()) {
            mainViewModel.updateTextTale(
                taleId,
                newTaleTitle,
                newTaleDescription
            )
            mainViewModel.fetchTextTales(context)
            navController?.popBackStack()
        } else {
            Toast.makeText(
                context,
                "Дайте название сказке и напишите её",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    SaveAlert (
        openDialog = openDialog.value,
        onSave = onSaveButton,
        onCancel = {
            openDialog.value = false
            navController?.popBackStack()
        }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                titleText = "Edit Screen",
                doNavigationIcon = {
                    if (newTaleTitle.isNotEmpty() && newTaleDescription.isNotEmpty()) {
                        openDialog.value = true
                    }
                },
                isOptionEnable = mainViewModel.isParent.value,
                false,
                null,
                navController
            )
        },
    ) { padding ->
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
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
                    color = MaterialTheme.colorScheme.onSurface,
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
                    placeholder = {
                        Text(
                            text = "Название",
                            fontSize = 20.sp
                        )
                    },
                    singleLine = true,
                    isError = isErrorTitle,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        errorTextColor = MaterialTheme.colorScheme.error,

                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        errorBorderColor = MaterialTheme.colorScheme.error,

                        cursorColor = MaterialTheme.colorScheme.primary
                    ),
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
                    placeholder = {
                        Text(
                            text = "Текст сказки",
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    },
                    isError = isErrorDescription,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        errorTextColor = MaterialTheme.colorScheme.error,

                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        errorBorderColor = MaterialTheme.colorScheme.error,

                        cursorColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.weight(0.05f))

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    onClick = {
                        onSaveButton()
                    }
                ) { Text(text = "Сохранить", fontSize = 20.sp) }

            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EditPreview() {
    val context = LocalContext.current
    val mainViewModel: MainViewModel = viewModel(factory = MyViewModelFactory(context))
    MainProjectTheme {
        EditTextTaleScreen(1, "", "", mainViewModel = mainViewModel)
    }
}