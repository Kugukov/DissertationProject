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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
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
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp)
            .clickable { doClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
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
                Text(text = taleName, modifier = Modifier.fillMaxWidth())
                Text(text = taleDescription, modifier = Modifier.fillMaxWidth())
            }
            Spacer(modifier = Modifier.weight(0.025f))

            cardButtons(Modifier)
//            Prev(modifier = Modifier.weight(0.25f))


//            IconButton(
//                enabled = isParent,
//                onClick = {/* TODO */ },
//                colors = IconButtonDefaults.iconButtonColors(
//                    containerColor = MaterialTheme.colorScheme.primary
//                ),
//                modifier = Modifier
//                    .clip(shape = CircleShape)
//                    .weight(0.15f)
//                    .aspectRatio(1f)
//            ) {
//                Icon(
//                    imageVector = Icons.Default.Edit,
//                    contentDescription = "Фоновое изображение",
//                    modifier = Modifier
//                        .padding(8.dp)
//                        .fillMaxSize(),
//                    tint = Color.White
//                )
//            }
//            Spacer(modifier = Modifier.weight(0.05f))
//            IconButton(
//                enabled = isParent,
//                onClick = {/* TODO */ },
//                colors = IconButtonDefaults.iconButtonColors(
//                    containerColor = MaterialTheme.colorScheme.primary
//                ),
//                modifier = Modifier
//                    .clip(shape = CircleShape)
//                    .weight(0.15f)
//                    .aspectRatio(1f)
//            ) {
//                Icon(
//                    imageVector = Icons.Default.Clear,
//                    contentDescription = "Фоновое изображение",
//                    modifier = Modifier
//                        .padding(8.dp)
//                        .fillMaxSize(),
//                    tint = Color.White
//                )
//            }
//            IconButton(
//                enabled = !isParent,
//                onClick = {/* TODO */ },
//                colors = IconButtonDefaults.iconButtonColors(
//                    containerColor = MaterialTheme.colorScheme.primary
//                ),
//                modifier = Modifier
//                    .clip(shape = CircleShape)
//                    .weight(0.15f)
//                    .aspectRatio(1f)
//            ) {
//                Icon(
//                    imageVector = Icons.Default.PlayArrow,
//                    contentDescription = "Фоновое изображение",
//                    modifier = Modifier
//                        .padding(8.dp)
//                        .fillMaxSize(),
//                    tint = Color.White
//                )
//            }
//            Spacer(modifier = Modifier.weight(0.05f))

        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CardItemPreview() {
    MainProjectTheme {
        CardItem("Название 1",
            "Описание 1",
            { modifier -> Prev(modifier) })
        { Log.d("CardItem", "check") }
    }
}

@Composable
fun Prev(modifier: Modifier) {
    Text("1")
}