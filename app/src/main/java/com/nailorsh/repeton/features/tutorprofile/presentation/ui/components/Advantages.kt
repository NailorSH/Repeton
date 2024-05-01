package com.nailorsh.repeton.features.tutorprofile.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.nailorsh.repeton.R

@Composable
fun Advantages(modifier: Modifier = Modifier) {
    // 100% refundable
    Row(modifier = modifier) {
        Icon(
            painter = painterResource(R.drawable.ic_baseline_verified_24),
            contentDescription = null,
            modifier = Modifier.padding(end = dimensionResource(R.dimen.padding_small))
        )
        Column {
            Text(
                text = stringResource(R.string.refund_policy_title),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = stringResource(R.string.refund_policy_description),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}