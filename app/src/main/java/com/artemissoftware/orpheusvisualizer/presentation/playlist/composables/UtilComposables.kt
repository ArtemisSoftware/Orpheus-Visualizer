package com.artemissoftware.orpheusvisualizer.presentation.playlist.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun screenHeight(): Dp = with(LocalContext.current) {
    return resources.displayMetrics.heightPixels.dp / LocalDensity.current.density
}
