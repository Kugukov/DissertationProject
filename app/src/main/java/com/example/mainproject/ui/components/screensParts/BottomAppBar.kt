package com.example.mainproject.ui.components.screensParts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BottomAppBar(
    audioButtonColor: Color,
    textButtonColor: Color,
    doClickMic: () -> Unit,
    doClickAdd: () -> Unit,
    doClickText: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.25f)
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.6f))
    ) {
        BottomAppBar(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            containerColor = Color.Transparent,
            tonalElevation = 0.dp,
        ) {
            Spacer(modifier = Modifier.weight(0.3f))

            FloatingActionButton(
                onClick = {
                    doClickMic()
                },
                shape = CircleShape,
                modifier = Modifier.padding(top = 30.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Mic,
                    tint = audioButtonColor,
                    contentDescription = "Home"
                )
            }

            Spacer(modifier = Modifier.weight(0.2f))

            FloatingActionButton(
                onClick = {
                    doClickAdd()
                },
                containerColor = MaterialTheme.colorScheme.tertiary,
                shape = CircleShape,
                modifier = Modifier
                    .padding(bottom = 30.dp)
                    .size(85.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    modifier = Modifier.size(50.dp)
                )
            }

            Spacer(modifier = Modifier.weight(0.2f))

            FloatingActionButton(
                onClick = {
                    doClickText()
                },
                shape = CircleShape,
                modifier = Modifier.padding(top = 30.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.TextFields,
                    tint = textButtonColor,
                    contentDescription = "TextScreen"
                )
            }

            Spacer(modifier = Modifier.weight(0.3f))
        }
    }
}