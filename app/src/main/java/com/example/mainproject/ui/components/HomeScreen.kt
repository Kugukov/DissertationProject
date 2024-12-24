package com.example.mainproject.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BedroomParent
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mainproject.R
import com.example.mainproject.models.AppNavController
import com.example.mainproject.ui.theme.MainProjectTheme
import com.example.mainproject.viewmodel.MainViewModel

@Composable
fun HomeScreen(navController: NavHostController ?= null) {

    var isPasswordEnable by remember { mutableStateOf(false) }
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.aquamarine))
    ) {
        Spacer(modifier = Modifier.weight(0.1f))

        IconButton(
            onClick = {
                isPasswordEnable = false
            },
            modifier = Modifier
                .clip(shape = CircleShape)
                .weight(0.15f)
                .aspectRatio(1f)
                .fillMaxSize()
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
                .weight(0.05f)
        )

        IconButton(
            onClick = {
                isPasswordEnable = true
            },
            modifier = Modifier
                .clip(shape = CircleShape)
                .weight(0.15f)
                .aspectRatio(1f)
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
                .weight(0.05f)
        )

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

        if (isPasswordEnable)
            TextField(
                value = "",
                onValueChange = {},
                modifier = Modifier.padding(top = 10.dp)
            )

        Spacer(modifier = Modifier.weight(0.1f))
    }

}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    MainProjectTheme {
        HomeScreen(navController = null)
    }
}