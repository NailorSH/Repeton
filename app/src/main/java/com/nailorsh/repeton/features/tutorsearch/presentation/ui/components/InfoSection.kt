package com.nailorsh.repeton.features.tutorsearch.presentation.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

@Composable
fun InfoSection(
    modifier: Modifier = Modifier,
    @StringRes title: Int,
    body: String
) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = modifier
    ) {
        Text(
            text = stringResource(title),
            style = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.colorScheme.onSurface
            )
        )
        Text(
            text = body,
            style = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}