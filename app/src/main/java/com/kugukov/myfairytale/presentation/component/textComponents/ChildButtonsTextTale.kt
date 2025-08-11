package com.kugukov.myfairytale.presentation.component.textComponents

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kugukov.myfairytale.presentation.component.CardItem
import com.kugukov.myfairytale.presentation.component.IconButtonForTaleCard

@Composable
fun ChildButtonsTextTale(
    getTextContent: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
    ) {
        IconButtonForTaleCard(
            imageVector = Icons.AutoMirrored.Filled.Message,
            iconContentDescription = "",
            onClick = { getTextContent() },
            modifier = modifier
        )
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
