package com.example.mainproject.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun ChildButtons(
    imageVector: ImageVector,
    getContent: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
    ) {
        // Кнопка изучения сказки
        IconButton(
            onClick = { getContent() },
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            modifier = Modifier
                .clip(shape = CircleShape)
                .weight(0.15f)
                .aspectRatio(1f)
                .shadow(6.dp, shape = CircleShape)
        ) {
            Icon(
                imageVector = imageVector,
                contentDescription = "Фоновое изображение",
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxSize(),
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}