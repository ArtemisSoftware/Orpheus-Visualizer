package com.artemissoftware.orpheusvisualizer.presentation.playlist

import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.artemissoftware.orpheusplaylist.headphone.presentation.playlist.AudioPlayerEvent
import com.artemissoftware.orpheusvisualizer.AudioPlayerState
import com.artemissoftware.orpheusvisualizer.domain.visualizer.VisualizerData
import com.artemissoftware.orpheusvisualizer.presentation.playlist.composables.LoadingDialog
import com.artemissoftware.orpheusvisualizer.presentation.playlist.composables.SongPage
import com.artemissoftware.orpheusvisualizer.presentation.playlist.composables.TracksSheet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayListScreen(
    state: AudioPlayerState,
    event: (AudioPlayerEvent) -> Unit,
    requestPermissionLauncher: ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>,
    visualizerData: State<VisualizerData>,
) {
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = rememberStandardBottomSheetState(skipHiddenState = false))

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {
            TracksSheet(
                state = state,
                onClick = {
                    scope.launch {
                        event(AudioPlayerEvent.Stop)
                        scaffoldState.bottomSheetState.partialExpand()
                        event(
                            AudioPlayerEvent.InitAudio(
                                audio = it,
                                context = context,
                                onAudioInitialized = {
                                    event(AudioPlayerEvent.Play)
                                },
                            ),
                        )
                    }
                },
            )
        },
        sheetPeekHeight = 32.dp,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            LoadingDialog(
                isLoading = state.isLoading,
                modifier = Modifier
                    .clip(shape = MaterialTheme.shapes.large)
                    .background(color = MaterialTheme.colorScheme.surface)
                    .requiredSize(size = 80.dp),
                onDone = {
                    event(AudioPlayerEvent.HideLoadingDialog)
                },
            )

            SongPage(
                state = state,
                event = event,
                context = context,
                sheetState = scaffoldState.bottomSheetState,
                scope = scope,
                visualizerData = visualizerData,
                requestPermissionLauncher = requestPermissionLauncher,
            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    onDismiss: () -> Unit,
    event: (AudioPlayerEvent) -> Unit,
    scope: CoroutineScope,
    modalBottomSheetState: SheetState,
    state: AudioPlayerState,
) {
    val context = LocalContext.current

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        TracksSheet(
            state = state,
            onClick = {
                scope.launch {
                    event(AudioPlayerEvent.Stop)
                    modalBottomSheetState.hide()
                    event(
                        AudioPlayerEvent.InitAudio(
                            audio = it,
                            context = context,
                            onAudioInitialized = {
                                event(AudioPlayerEvent.Play)
                            },
                        ),
                    )
                }
            },
        )
    }
}
