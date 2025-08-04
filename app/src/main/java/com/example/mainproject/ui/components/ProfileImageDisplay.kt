package com.example.mainproject.ui.components

import android.graphics.Bitmap
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.imageResource

@Composable
fun ProfileImageDisplay(
    savedImage: Bitmap?,
    @DrawableRes defaultImageRes: Int
) {
    val imageToShow: ImageBitmap = when {
        savedImage != null -> savedImage.asImageBitmap()
        else -> ImageBitmap.imageResource(id = defaultImageRes)
    }

    Image(
        bitmap = imageToShow,
        contentDescription = null,
        modifier = Modifier.fillMaxSize()
    )
}
