package com.nailorsh.repeton.features.tutorprofile.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nailorsh.repeton.R

@Composable
fun BottomTutorProfileBar(
    onChatButtonClicked: () -> Unit,
    onTrialLessonButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    BottomAppBar(
        tonalElevation = 1.dp,
        contentPadding = PaddingValues(horizontal = dimensionResource(R.dimen.padding_medium)),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Go to chat button
            OutlinedButton(onClick = onChatButtonClicked) {
                Icon(
                    painterResource(R.drawable.ic_baseline_chat_24),
                    contentDescription = stringResource(R.string.chat),
                )
            }

            // Trial lesson button
            Button(
                onClick = onTrialLessonButtonClicked,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    painterResource(R.drawable.ic_round_local_library_24),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.take_trial_lesson),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(end = dimensionResource(R.dimen.padding_medium))
                )
            }
        }
    }
}