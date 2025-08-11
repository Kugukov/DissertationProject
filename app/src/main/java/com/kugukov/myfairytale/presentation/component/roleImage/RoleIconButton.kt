package com.kugukov.myfairytale.presentation.component.roleImage

import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun RoleIconButton(
    isSelected: Boolean,
    imageUri: Uri?,
    @DrawableRes defaultImageRes: Int,
    label: String,
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(240.dp)
            .clip(CircleShape)
            .border(
                width = if (isSelected) 5.dp else 1.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                shape = CircleShape
            )
    ) {
        ProfileImageDisplay(imageUri = imageUri, defaultImageRes)
    }

    Spacer(modifier = Modifier.height(8.dp))

    Text(
        text = label,
        color = MaterialTheme.colorScheme.primary,
        fontSize = MaterialTheme.typography.titleLarge.fontSize
    )
}