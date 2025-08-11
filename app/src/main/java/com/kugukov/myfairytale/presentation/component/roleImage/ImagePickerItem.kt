package com.kugukov.myfairytale.presentation.component.roleImage

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun ImagePickerItem(
    title: String,
    savedUri: Uri?,
    tempUri: Uri?,
    @DrawableRes defaultImageRes: Int,
    onPickImage: (Uri?) -> Unit
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> onPickImage(uri) }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(title)

        val painter = when {
            savedUri != null -> savedUri
            tempUri != null -> tempUri
            else -> defaultImageRes
        }

        AsyncImage(
            model = painter,
            contentDescription = title,
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .fillMaxSize()
                .clickable { launcher.launch("image/*") }
        )
    }
}
