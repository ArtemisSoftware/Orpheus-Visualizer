package com.artemissoftware.orpheusvisualizer.presentation.playlist.composables

import android.Manifest
import android.content.Context
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.artemissoftware.orpheusplaylist.headphone.presentation.playlist.AudioPlayerEvent
import com.artemissoftware.orpheusvisualizer.AudioPlayerState
import com.artemissoftware.orpheusvisualizer.R
import com.artemissoftware.orpheusvisualizer.domain.visualizer.VisualizerData
import com.artemissoftware.orpheusvisualizer.presentation.util.Constants.FORWARD_BACKWARD_STEP
import com.artemissoftware.orpheusvisualizer.ui.theme.Black3
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongPage(
    state: AudioPlayerState,
    event: (AudioPlayerEvent) -> Unit,
    visualizerData: State<VisualizerData>,
    context: Context,
    scope: CoroutineScope,
    requestPermissionLauncher: ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>,
    sheetState: SheetState,
) {
    val screenHeight = screenHeight()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState())
            .background(color = MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TopBar(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
                .requiredHeight(height = 80.dp),
            leadingIcon = {
                LikeButton(
                    isLiked = state.likedSongs.contains(state.selectedAudio.songId),
                    enabled = state.selectedAudio.isNotEmpty(),
                    onClick = {
                        event.invoke(AudioPlayerEvent.LikeOrNotSong(id = state.selectedAudio.songId))
                    },
                )
            },
            title = {
                if (state.selectedAudio.isNotEmpty()) {
                    val artist = if (state.selectedAudio.artist.contains(
                            "unknown",
                            ignoreCase = true,
                        )
                    ) {
                        ""
                    } else {
                        "${state.selectedAudio.artist} - "
                    }
                    Text(
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(text = artist)
                            }
                            append(text = "  ${state.selectedAudio.songTitle}")
                        },
                        color = MaterialTheme.colorScheme.onSurface,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.labelSmall,
                    )
                }
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        setupPermissions(
                            context = context,
                            permissions = arrayOf(
                                Manifest.permission.RECORD_AUDIO,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                            ),
                            launcher = requestPermissionLauncher,
                            onPermissionsGranted = {
                                scope.launch {
                                    if (state.audios.isEmpty()) {
                                        event.invoke(AudioPlayerEvent.LoadMedias)
                                    }
                                    sheetState.show()
                                }
                            },
                        )
                    },
                    content = {
                        Icon(
                            painter = painterResource(id = R.drawable.up_right_from_square_solid),
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.onSurface,
                        )
                    },
                )
            },
        )

        Spacer(modifier = Modifier.requiredHeight(height = 16.dp))

        AlbumCover(
            cover = state.selectedAudio.cover,
            modifier = Modifier.requiredHeight(height = screenHeight * 0.35f).padding(horizontal = 16.dp),
        )

        Spacer(modifier = Modifier.requiredHeight(height = 16.dp))

        StackedBarVisualizer(
            modifier = Modifier
                .fillMaxWidth()
                .height(height = 140.dp)
                .padding(vertical = 4.dp, horizontal = 8.dp),
            shape = MaterialTheme.shapes.large,
            barCount = 32,
            barColors = listOf(
                Color(0xFF1BEBE9),
                Color(0xFF39AFEA),
                Color(0xFF0291D8),
            ),
            stackBarBackgroundColor = if (isSystemInDarkTheme()) {
                Black3
            } else {
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f)
            },
            data = visualizerData.value,
        )

        Spacer(modifier = Modifier.requiredHeight(height = 10.dp))

        TimeBar(
            currentPosition = state.currentPosition,
            onValueChange = { position ->
                event.invoke(AudioPlayerEvent.Seek(position = position))
            },
            duration = state.selectedAudio.duration,
        )

        Spacer(modifier = Modifier.requiredHeight(height = 10.dp))

        TrackBar(
            currentPosition = state.currentPosition,
            isPlaying = state.isPlaying,
            selectedAudio = state.selectedAudio,
            step = FORWARD_BACKWARD_STEP,
            onFastBackward = {
                event.invoke(AudioPlayerEvent.Seek(position = state.currentPosition - FORWARD_BACKWARD_STEP.toFloat()))
            },
            onPlay = {
                event.invoke(AudioPlayerEvent.Play)
            },
            onPause = {
                event.invoke(AudioPlayerEvent.Pause)
            },
            onFastForward = {
                event.invoke(AudioPlayerEvent.Seek(position = state.currentPosition + FORWARD_BACKWARD_STEP.toFloat()))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 10.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SongPagePreview() {
//    SongPage()
}
