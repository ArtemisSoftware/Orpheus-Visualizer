package com.artemissoftware.orpheusvisualizer.presentation.playlist.composables

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.artemissoftware.orpheusvisualizer.R

@Composable
fun WarningMessage(
    text: String,
    @DrawableRes iconResId: Int,
    modifier: Modifier = Modifier,
    trailingContent: (@Composable () -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .fillMaxWidth(fraction = 0.90f),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            painter = painterResource(id = iconResId),
            tint = MaterialTheme.colorScheme.onSurface,
            contentDescription = "",
        )

        Spacer(modifier = Modifier.padding(vertical = 8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.displaySmall,
            )
            trailingContent?.invoke()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WarningMessagePreview() {
    WarningMessage(
        text = "txt_no_media",
        iconResId = R.drawable.circle_info_solid,
        modifier = Modifier.padding(vertical = 16.dp),
    )
}
