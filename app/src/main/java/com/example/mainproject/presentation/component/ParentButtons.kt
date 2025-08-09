package com.example.mainproject.presentation.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp

@Composable
fun ParentButtons(
    onNavigateToEditTale: () -> Unit,
    doDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
    ) {
        IconButton(
            onClick = { onNavigateToEditTale() },
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier
                .clip(shape = CircleShape)
                .border(2.dp, MaterialTheme.colorScheme.onPrimaryContainer, shape = CircleShape)
                .weight(0.45f)
                .aspectRatio(1f)
                .shadow(6.dp, shape = CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit",
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxSize(),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        Spacer(modifier = Modifier.weight(0.1f))

        IconButton(
            onClick = { doDelete() },
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            ),
            modifier = Modifier
                .clip(shape = CircleShape)
                .border(2.dp, MaterialTheme.colorScheme.onPrimaryContainer, shape = CircleShape)
                .weight(0.45f)
                .aspectRatio(1f)
                .shadow(6.dp, shape = CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = "Delete",
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxSize(),
                tint = MaterialTheme.colorScheme.onErrorContainer
            )
        }
    }
}