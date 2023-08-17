package com.artemissoftware.orpheusvisualizer.presentation.playlist.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.artemissoftware.orpheusvisualizer.R
import com.artemissoftware.orpheusvisualizer.data.model.AudioMetadata

@Composable
fun TrackBar(
    currentPosition: Int,
    isPlaying: Boolean,
    selectedAudio: AudioMetadata,
    step: Int,
    onPlay: () -> Unit,
    onPause: () -> Unit,
    onFastForward: () -> Unit,
    onFastBackward: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
    ) {
        FastButton(
            enabled = currentPosition > step,
            onClick = onFastBackward,
            iconResId = R.drawable.backward_solid,
            stringResId = R.string.lbl_fast_backward,
        )
        PlayPauseButton(
            modifier = Modifier.padding(horizontal = 26.dp),
            enabled = selectedAudio.isNotEmpty(),
            isPlaying = isPlaying,
            onPlay = onPlay,
            onPause = onPause,
        )
        FastButton(
            enabled = currentPosition < (selectedAudio.duration - step),
            onClick = onFastForward,
            iconResId = R.drawable.forward_solid,
            stringResId = R.string.lbl_fast_forward,
        )
    }
}
