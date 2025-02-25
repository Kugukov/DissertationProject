package com.example.mainproject.ui.components

import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.mainproject.R
import com.example.mainproject.models.MyViewModelFactory
import com.example.mainproject.ui.theme.MainProjectTheme
import com.example.mainproject.viewmodel.MainViewModel
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(viewModel: MainViewModel, navController: NavHostController? = null) {

    var isPasswordEnable by remember { mutableStateOf(true) }
    val context = LocalContext.current

    var childImage by remember { mutableStateOf<Bitmap?>(null) }
    var parentImage by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(Unit) {
        viewModel.loadImage(context, "saved_child_image.jpg")
        viewModel.loadImage(context, "saved_parent_image.jpg")
        childImage = viewModel.childImage
        parentImage = viewModel.parentImage
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.aquamarine))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(0.70f)
        ) {
            Spacer(modifier = Modifier.weight(0.05f))

            IconButton(
                onClick = {
                    isPasswordEnable = false
                },
                modifier = Modifier
                    .clip(shape = CircleShape)
                    .weight(0.15f)
                    .aspectRatio(1f)
                    .fillMaxSize()
                    .then(
                        if (!isPasswordEnable) Modifier.border(
                            5.dp,
                            Color.Green,
                            shape = CircleShape
                        ) else Modifier.border(0.dp, Color.Green)
                    )
            ) {
                if (childImage != null) {
                    childImage?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = "Выбранное изображение",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.boy),
                        contentDescription = "Выбранное изображение",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Text(

                text = "Children",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(0.025f)
            )

            IconButton(
                onClick = {
                    isPasswordEnable = true
                },
                modifier = Modifier
                    .clip(shape = CircleShape)
                    .weight(0.15f)
                    .aspectRatio(1f)
                    .then(
                        if (isPasswordEnable) Modifier.border(
                            5.dp,
                            Color.Green,
                            shape = CircleShape
                        ) else Modifier.border(0.dp, Color.Green)
                    )
            ) {
                if (parentImage == null) {
                    Image(
                        painter = painterResource(id = R.drawable.parent),
                        contentDescription = "Выбранное изображение",
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    parentImage?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = "Выбранное изображение",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

            Text(
                text = "Parent",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(0.025f)
            )

        }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .weight(0.13f)
        ) {
            AnimatedVisibility(
                visible = isPasswordEnable,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically(),
            ) {
                OutlinedTextField(
                    value = viewModel.passwordValue.value,
                    label = { Text("Password") },
                    onValueChange = { newPassword ->
                        viewModel.updatePasswordValue(newPassword)
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledContainerColor = Color(0xFF6650a4),
                        unfocusedContainerColor = Color(0xFF445e91),
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = PasswordVisualTransformation()
                )
            }
        }

        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .weight(0.17f)
                .padding(bottom = 55.dp)
        ) {
            Button(
                onClick = {
                    if (viewModel.passwordValue.value == viewModel.password.value) {
                        navController?.navigate("audioScreen")
                    } else {
                        Toast.makeText(context, "False password", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .clip(shape = CircleShape)
                    .weight(0.05f)
            ) {
                Text(text = "Sign in")
            }
        }

    }

}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    val context = LocalContext.current
    val viewModel: MainViewModel = viewModel(factory = MyViewModelFactory(context))

    MainProjectTheme {
        HomeScreen(viewModel = viewModel, navController = null)
    }
}