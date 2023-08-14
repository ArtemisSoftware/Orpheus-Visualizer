package com.artemissoftware.orpheusvisualizer.domain.repository

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.artemissoftware.orpheusvisualizer.data.model.AudioMetadata
import kotlinx.coroutines.flow.Flow

interface AudioPlayerRepository {

    suspend fun loadCoverBitmap(context: Context, uri: Uri): Bitmap?

    suspend fun getAudios(): List<AudioMetadata>

    suspend fun likeOrNotSong(id: Long)

    fun getLikedSongs(): Flow<List<Long>>
}
