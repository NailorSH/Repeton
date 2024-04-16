package com.nailorsh.repeton.features.tutorprofile.presentation.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.nailorsh.repeton.R
import com.nailorsh.repeton.core.ui.components.InfoBlockWithLabel

@Composable
fun ExperienceYearsNumber(
    yearsNumber: Int,
    modifier: Modifier = Modifier,
) {
    val yearsText = when {
        yearsNumber == 1 || yearsNumber > 20 && yearsNumber % 10 == 1 -> "год"
        yearsNumber in 2..4 || yearsNumber > 20 && yearsNumber % 10 in 2..4 -> "года"
        else -> "лет"
    }

    InfoBlockWithLabel(
        label = stringResource(R.string.experience_info_label),
        modifier = modifier
    ) {
        Text(
            text = "$yearsNumber $yearsText",
            style = MaterialTheme.typography.titleLarge
        )
    }
}