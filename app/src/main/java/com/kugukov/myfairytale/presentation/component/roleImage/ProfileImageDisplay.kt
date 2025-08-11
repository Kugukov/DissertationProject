package com.kugukov.myfairytale.presentation.component.roleImage

import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage

@Composable
fun ProfileImageDisplay(
    imageUri: Uri?,
    @DrawableRes defaultImageRes: Int
) {
    AsyncImage(
        model = imageUri ?: defaultImageRes,
        contentDescription = "Selected image",
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize(),
    )
}
