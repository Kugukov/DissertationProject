package com.example.mainproject.ui.components.screensParts

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mainproject.ui.components.CardItem

@Composable
fun ChildButtonsTextTale(
    getTextContent: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
    ) {
        IconButton(
            onClick = {
                getTextContent()
            },
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier
                .clip(shape = CircleShape)
                .weight(0.45f)
                .border(2.dp, MaterialTheme.colorScheme.onPrimaryContainer, shape = CircleShape)
                .aspectRatio(1f)
                .shadow(6.dp, shape = CircleShape)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Message,
                contentDescription = "Читать",
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxSize(),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ChildButtonsTextTalePreview() {
    MaterialTheme {
        CardItem(
            taleName = "testTale",
            taleDescription = "testDesc",
            cardButtons = {
                ChildButtonsTextTale(
                    getTextContent = { },
                    modifier = Modifier.size(100.dp)
                )
            },
            doClick = {},
            modifier = Modifier
        )
    }
}
