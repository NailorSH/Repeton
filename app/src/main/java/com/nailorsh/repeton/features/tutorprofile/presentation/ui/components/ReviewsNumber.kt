package com.nailorsh.repeton.features.tutorprofile.presentation.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.nailorsh.repeton.R
import com.nailorsh.repeton.core.ui.components.InfoBlockWithLabel

@Composable
fun ReviewsNumber(
    reviewsNumber: Int,
    modifier: Modifier = Modifier
) {
    InfoBlockWithLabel(
        label = stringResource(R.string.reviews_label),
        modifier = modifier
    ) {
        Text(
            text = reviewsNumber.toString(),
            style = MaterialTheme.typography.titleLarge
        )
    }
}