package com.nailorsh.repeton.features.tutorprofile.presentation.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nailorsh.repeton.R
import com.nailorsh.repeton.core.ui.theme.LightBlue
import com.nailorsh.repeton.core.ui.theme.LightGreen

@Composable
fun LanguageItem(name: String, level: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = dimensionResource(R.dimen.padding_extra_small)),
        verticalAlignment = Alignment.CenterVertically,

        ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyLarge,
        )
        Surface(
            modifier = Modifier
                .padding(start = dimensionResource(R.dimen.padding_small))
                .clip(RoundedCornerShape(dimensionResource(R.dimen.padding_medium))),
            color = when (level) {
                stringResource(R.string.native_speaker) -> LightGreen
                else -> LightBlue
            },
            shadowElevation = 1.dp
        ) {
            Text(
                text = level,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(
                    horizontal = dimensionResource(R.dimen.padding_medium),
                    vertical = dimensionResource(R.dimen.padding_small)
                )
            )
        }
    }
}