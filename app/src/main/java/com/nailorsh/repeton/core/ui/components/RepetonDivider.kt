package com.nailorsh.repeton.core.ui.components

import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.nailorsh.repeton.R
import com.nailorsh.repeton.core.ui.theme.LineColor

@Composable
fun RepetonDivider(modifier: Modifier = Modifier) {
    HorizontalDivider(
        modifier = modifier
            .width(dimensionResource(R.dimen.divider_width)),
        thickness = dimensionResource(R.dimen.divider_thickness),
        color = LineColor
    )
}