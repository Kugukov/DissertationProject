package com.example.mainproject.ui.components

import android.util.Log
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
import com.example.mainproject.R
import com.example.mainproject.ui.theme.MainProjectTheme

@Composable
fun CardItem(
    taleName: String,
    taleDescription: String,
    cardButtons: @Composable (Modifier) -> Unit,
    doClick: () -> Unit
) {
    /* TODO клик на карточку*/
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp)
            .shadow(8.dp, shape = RoundedCornerShape(16.dp))
            .clickable { doClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.parent),
                contentDescription = "Фоновое изображение",
                modifier = Modifier
                    .weight(0.15f)
                    .clip(shape = CircleShape),
            )
            Spacer(modifier = Modifier.weight(0.025f))
            Column(
                modifier = Modifier
                    .weight(0.5f)
            ) {
                Text(
                    text = taleName,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.onSurfaceVariant ,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = taleDescription,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.weight(0.025f))

            cardButtons(Modifier)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CardItemPreview() {
    MainProjectTheme(darkTheme = true, dynamicColor = false) {
        CardItem("Название 1",
            "Описание 1",
            { Text("1") })
        { Log.d("CardItem", "check") }
    }
}