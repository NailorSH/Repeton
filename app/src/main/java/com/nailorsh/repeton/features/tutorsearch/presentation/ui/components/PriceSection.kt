package com.nailorsh.repeton.features.tutorsearch.presentation.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nailorsh.repeton.common.data.models.lesson.SubjectWithPrice

const val RUBLE = "₽"

@Composable
fun PriceSection(
    modifier: Modifier = Modifier,
    @StringRes title: Int,
    subjectsPrices: List<SubjectWithPrice>
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.Top),
        horizontalAlignment = Alignment.Start,
        modifier = modifier
    ) {
        Text(
            text = stringResource(title),
            style = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.colorScheme.onSurface
            )
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(3.dp, Alignment.Top)
        ) {
            subjectsPrices.forEach {
                SubjectWithPriceItem(it)
            }
        }
    }
}

@Composable
fun SubjectWithPriceItem(
    subjectWithPrice: SubjectWithPrice,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = subjectWithPrice.subject.name,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "${subjectWithPrice.price} $RUBLE",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Right,
            modifier = Modifier.weight(1f)
        )
    }
}