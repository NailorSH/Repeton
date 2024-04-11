package com.nailorsh.repeton.features.newlesson.presentation.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nailorsh.repeton.R
import com.nailorsh.repeton.core.ui.components.RepetonDivider

@Composable
fun NewLessonTopBar(modifier: Modifier = Modifier) {
    Spacer(modifier = modifier.height(48.dp))
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        ),
        modifier = modifier
            .width(dimensionResource(R.dimen.schedule_screen_button_width))

    ) {
        Box(
            contentAlignment = Alignment.Center, modifier = modifier
                .height(48.dp)
                .fillMaxWidth()
        ) {
            Text(
                style = MaterialTheme.typography.headlineSmall,
                text = stringResource(R.string.new_lesson_screen_headline),
                letterSpacing = 0.5.sp,
            )
        }

    }
    RepetonDivider(
        modifier = modifier
            .padding(vertical = dimensionResource(R.dimen.padding_medium))
    )
}
