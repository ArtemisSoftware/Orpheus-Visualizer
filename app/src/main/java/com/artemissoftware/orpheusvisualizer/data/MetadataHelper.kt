package com.artemissoftware.orpheusvisualizer.data

import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import androidx.annotation.WorkerThread
import com.artemissoftware.orpheusvisualizer.data.model.AudioMetadata
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MetadataHelper @Inject constructor(@ApplicationContext val context: Context) {

    @WorkerThread
    fun getAudios() = getCursorData()

    @WorkerThread
    fun getAlbumArt(context: Context, uri: Uri): Bitmap? {
        val mediaMetadataRetriever = MediaMetadataRetriever()
        mediaMetadataRetriever.setDataSource(context, uri)

        val bitmap: Bitmap? = try {
            val data = mediaMetadataRetriever.embeddedPicture
            if (data != null) {
                BitmapFactory.decodeByteArray(data, 0, data.size)
            } else {
                null
            }
        } catch (exp: Exception) {
            null
        } finally {
            mediaMetadataRetriever.release()
        }
        return bitmap
    }

    private fun getCursorData(): MutableList<AudioMetadata> {
        val audioList = mutableListOf<AudioMetadata>()

        val cursor = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            PROJECTION,
            SELECTION_CLAUSE,
            SELECTION_ARG,
            SORT_ORDER,
        )

        cursor?.let { cursorIndex ->

            val idColumn =
                cursorIndex.getColumnIndexOrThrow(getColumn(MediaStore.Audio.Media._ID))

            val durationColumn =
                cursorIndex.getColumnIndexOrThrow(getColumn(MediaStore.Audio.Media.DURATION))

            val titleColumn =
                cursorIndex.getColumnIndexOrThrow(getColumn(MediaStore.Audio.Media.TITLE))

            val artistColumn =
                cursorIndex.getColumnIndexOrThrow(getColumn(MediaStore.Audio.Media.ARTIST))

            cursorIndex.apply {
                if (count > 0) {
                    while (cursorIndex.moveToNext()) {
                        val id = cursorIndex.getLong(idColumn)
                        val duration = cursorIndex.getInt(durationColumn)
                        val title = cursorIndex.getString(titleColumn)
                        val artist = cursorIndex.getString(artistColumn)
                        val contentUri: Uri = ContentUris.withAppendedId(
                            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                            id,
                        )

                        // TODO: não obter o cover logo mudar isto quando juntar os dois códigos
                        audioList += AudioMetadata(
                            songId = id,
                            contentUri = contentUri,
                            cover = null, // Time consuming operation to get this. Better do it on demand
                            songTitle = title,
                            artist = artist,
                            duration = duration,
                        )
                    }
                }
            }
        }
        return audioList
    }

    private fun getColumn(mediaId: String): String? {
        return PROJECTION.find { it == mediaId }
    }

    private companion object {

        private val PROJECTION = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.ARTIST,
        )

        private const val SELECTION_CLAUSE = "${MediaStore.Audio.AudioColumns.IS_MUSIC} = ?"
        private val SELECTION_ARG = arrayOf("1")
        private const val SORT_ORDER = "${MediaStore.Audio.AudioColumns.DISPLAY_NAME} ASC"
    }
}
