package com.artemissoftware.orpheusvisualizer

import com.artemissoftware.orpheusvisualizer.data.model.AudioMetadata

data class AudioPlayerState(
    val isLoading: Boolean = false,
    val audios: List<AudioMetadata> = emptyList(),
    val isPlaying: Boolean = false,
    val selectedAudio: AudioMetadata = AudioMetadata(),
    val currentPosition: Int = 0,
    val likedSongs: List<Long> = emptyList(),
)
