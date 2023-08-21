package com.artemissoftware.orpheusvisualizer.presentation.playlist.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TimeBar(
    currentPosition: Int,
    duration: Int,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = milliSecondsToTimeString(milliseconds = currentPosition),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Slider(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth(fraction = 0.9f),
            value = currentPosition.toFloat(),
            onValueChange = { onValueChange(it) },
            valueRange = 0f..duration.toFloat(),
        )
        Text(
            text = milliSecondsToTimeString(milliseconds = duration),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

fun milliSecondsToTimeString(milliseconds: Int): String {
    var result = ""
    var secondsString = ""
    val hours = (milliseconds / (1000 * 60 * 60))
    val minutes = (milliseconds % (1000 * 60 * 60)) / (1000 * 60)
    val seconds = (milliseconds % (1000 * 60 * 60) % (1000 * 60) / 1000)

    if (hours > 0) {
        result = "$hours:"
    }
    secondsString = if (seconds < 10) {
        "0$seconds"
    } else {
        "$seconds"
    }
    result = "$result$minutes:$secondsString"
    return result
}
