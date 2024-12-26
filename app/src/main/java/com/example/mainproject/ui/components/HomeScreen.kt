package com.example.mainproject.ui.components

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BedroomParent
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mainproject.R
import com.example.mainproject.models.AppNavController
import com.example.mainproject.ui.theme.MainProjectTheme
import com.example.mainproject.viewmodel.MainViewModel

@Composable
fun HomeScreen(navController: NavHostController? = null) {

    var isPasswordEnable by remember { mutableStateOf(true) }
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
                Icon(
                    painter = painterResource(id = R.drawable.boy),
                    tint = Color.Unspecified,
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxSize()
                )
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
                Icon(
                    painter = painterResource(id = R.drawable.parent),
                    tint = Color.Unspecified,
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxSize()
                )
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
                    value = "Text",
                    label = { Text("Password") },
                    onValueChange = {},
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledContainerColor = Color(0xFF6650a4),
                        unfocusedContainerColor = Color(0xFF445e91),
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
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
                    navController?.navigate("audioScreen")
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
    MainProjectTheme {
        HomeScreen(navController = null)
    }
}