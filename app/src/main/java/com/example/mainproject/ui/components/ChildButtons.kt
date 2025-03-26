package com.example.mainproject.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun ChildButtons(
    route: String,
    imageVector: ImageVector,
    getContent: () -> Unit,
    navController: NavHostController?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
    ) {
        // Кнопка изучения сказки
        IconButton(
            onClick = { getContent() },
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .clip(shape = CircleShape)
                .weight(0.15f)
                .aspectRatio(1f)
        ) {
            Icon(
                imageVector = imageVector,
                contentDescription = "Фоновое изображение",
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxSize(),
                tint = Color.White
            )
        }
    }
}