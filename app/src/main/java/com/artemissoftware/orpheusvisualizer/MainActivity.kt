package com.artemissoftware.orpheusvisualizer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.artemissoftware.orpheusplaylist.headphone.presentation.playlist.AudioPlayerEvent
import com.artemissoftware.orpheusvisualizer.presentation.playlist.PlayListScreen
import com.artemissoftware.orpheusvisualizer.presentation.playlist.composables.showPermissionsRationalDialog
import com.artemissoftware.orpheusvisualizer.ui.theme.OrpheusVisualizerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var requestPermissionLauncher: ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current

            val dialogText = stringResource(id = R.string.txt_permissions)

            val errorText = stringResource(id = R.string.txt_error_app_settings)

            requestPermissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestMultiplePermissions(),
                onResult = { permissions ->
                    val permissionsGranted = permissions.values.reduce { acc, next -> acc && next }
                    if (!permissionsGranted) {
                        showPermissionsRationalDialog(
                            context = context,
                            okButtonTextResId = R.string.lbl_ok,
                            cancelButtonTextResId = R.string.lbl_cancel,
                            dialogText = dialogText,
                            errorText = errorText,
                            packageName = packageName,
                        )
                    } else {
                        mainViewModel.onEvent(AudioPlayerEvent.LoadMedias)
                    }
                },
            )

            OrpheusVisualizerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    PlayListScreen(
                        state = mainViewModel.state.collectAsState().value,
                        event = mainViewModel::onEvent,
                        visualizerData = mainViewModel.visualizerData,
                        requestPermissionLauncher = requestPermissionLauncher,
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    OrpheusVisualizerTheme {
        Greeting("Android")
    }
}
