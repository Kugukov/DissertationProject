package com.kugukov.myfairytale.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kugukov.myfairytale.R
import com.kugukov.myfairytale.presentation.component.audioComponents.ChildButtonsAudioTale
import com.kugukov.myfairytale.presentation.theme.MainProjectTheme

@Composable
fun CardItem(
    taleName: String,
    taleDescription: String,
    cardButtons: @Composable (Modifier) -> Unit,
    doClick: () -> Unit,
    modifier: Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp)
            .shadow(8.dp, shape = RoundedCornerShape(16.dp))
            .clickable { doClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.parent),
                contentDescription = "Background image",
                modifier = modifier
                    .weight(0.15f)
                    .clip(shape = CircleShape),
            )
            Spacer(modifier = modifier.weight(0.025f))
            Column(
                modifier = modifier
                    .weight(0.5f)
            ) {
                Text(
                    text = taleName,
                    modifier = modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = taleDescription,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    modifier = modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = modifier.weight(0.025f))

            cardButtons(modifier)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CardItemPreview() {
    MainProjectTheme(darkTheme = false) {
        CardItem(
            taleName = "Name 1",
            taleDescription = "Description 1",
            cardButtons = { modifier ->
                ChildButtonsAudioTale(
                    isPaused = true,
                    isThisPlaying = false,
                    onPlayPauseClick = { },
                    onStopPlayClick = { },
                )
            },
            doClick = {},
            modifier = Modifier
        )
    }
}