package com.artemissoftware.orpheusvisualizer.presentation.playlist.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.artemissoftware.orpheusvisualizer.AudioPlayerState
import com.artemissoftware.orpheusvisualizer.R
import com.artemissoftware.orpheusvisualizer.data.model.AudioMetadata

@Composable
fun TrackList(
    state: AudioPlayerState,
    onClick: (AudioMetadata) -> Unit = {},
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(id = R.string.lbl_tracks),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .padding(
                    bottom = 3.dp,
                    top = 12.dp,
                ),
            textDecoration = TextDecoration.Underline,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }

    state.audios.forEach { audio ->
        Track(
            audio = audio,
            isPlaying = audio.songId == state.selectedAudio.songId,
            modifier = Modifier
                .padding(
                    horizontal = 8.dp,
                    vertical = 10.dp,
                )
                .requiredHeight(height = 100.dp),
            onClick = onClick,
        )
        Divider(modifier = Modifier.padding(horizontal = 8.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun TrackListPreview() {
    TrackList(AudioPlayerState())
}
