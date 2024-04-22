package com.nailorsh.repeton.features.tutorprofile.presentation.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.nailorsh.repeton.R

@Composable
fun AllSubjectsButton(
    onAllSubjectsButtonClicked: () -> Unit
) {
    TextButton(
        onClick = onAllSubjectsButtonClicked,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(R.dimen.padding_medium))
    ) {
        Text(
            text = stringResource(R.string.other_subjects),
            style = MaterialTheme.typography.titleMedium
        )
    }
}