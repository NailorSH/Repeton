package com.nailorsh.repeton.features.tutorprofile.presentation.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.nailorsh.repeton.R
import com.nailorsh.repeton.core.ui.components.InfoBlockWithLabel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun PriceInfo(
    price: Int,
    modifier: Modifier = Modifier,
    currencyLocale: Locale = Locale("ru", "RU"), // По умолчанию - российский рубль
) {
    val currencyFormat = NumberFormat.getCurrencyInstance(currencyLocale).apply {
        maximumFractionDigits = 0
    }

    InfoBlockWithLabel(
        label = stringResource(R.string.for_lesson_label),
        modifier = modifier
    ) {
        Text(
            text = currencyFormat.format(price),
            style = MaterialTheme.typography.titleLarge
        )
    }
}