package com.example.mainproject.ui.components.screensParts

import androidx.compose.foundation.border
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
fun IconButtonForTaleCard(
    imageVector: ImageVector,
    iconContentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier
) {
    IconButton(
        onClick = {
            onClick()
        },
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier
            .clip(shape = CircleShape)
            .border(2.dp, MaterialTheme.colorScheme.onPrimaryContainer, shape = CircleShape)
            .aspectRatio(1f)
            .shadow(6.dp, shape = CircleShape)
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = iconContentDescription,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize(),
            tint = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}