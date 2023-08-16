package com.artemissoftware.orpheusvisualizer.presentation.playlist.composables

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.artemissoftware.orpheusvisualizer.R

@Composable
fun AudioCover(
    bitmap: Bitmap? = null,
    screenHeight: Dp,
) {
    bitmap?.let {
        Image(
            bitmap = it.asImageBitmap(),
            modifier = Modifier
                .requiredHeight(height = screenHeight * 0.4f)
                .clip(shape = MaterialTheme.shapes.large),
            contentScale = ContentScale.Crop,
            contentDescription = "",
        )
    } ?: Box(
        modifier = Modifier.requiredHeight(height = screenHeight * 0.4f),
        contentAlignment = Alignment.Center,
    ) {
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            shape = MaterialTheme.shapes.large,
            modifier = Modifier.fillMaxHeight(fraction = 0.5f),
        ) {
            Image(
                painter = painterResource(id = R.drawable.musical_note_music_svgrepo_com),
                modifier = Modifier
                    .padding(
                        top = 25.dp,
                        bottom = 26.dp,
                        start = 8.dp,
                        end = 20.dp,
                    ),
                contentScale = ContentScale.FillHeight,
                contentDescription = "",
            )
        }
    }
}
