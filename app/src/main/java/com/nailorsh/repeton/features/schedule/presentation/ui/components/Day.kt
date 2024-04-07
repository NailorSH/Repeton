package com.nailorsh.repeton.features.schedule.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nailorsh.repeton.R
import java.time.DayOfWeek
import java.time.LocalDate

@Composable
fun Day(day: LocalDate, selectedDay: LocalDate, hasLessons: Boolean, onSelectDay: () -> Unit) {
    val selected = day.equals(selectedDay)
    val backgroundColor =
        if (selected)
            MaterialTheme.colorScheme.tertiary
        else if (hasLessons)
            MaterialTheme.colorScheme.tertiaryContainer
        else MaterialTheme.colorScheme.surfaceContainerHighest

    val textColor =
        if (selected)
            MaterialTheme.colorScheme.onTertiary
        else if (hasLessons)
            MaterialTheme.colorScheme.onTertiaryContainer
        else MaterialTheme.colorScheme.onSurfaceVariant


    val numberTextStyle = MaterialTheme.typography.headlineSmall
    val dayTextStyle = MaterialTheme.typography.labelSmall
    val dayFontWeight = FontWeight.W400

    Box(
        modifier = Modifier
            .width(36.dp)
            .height(48.dp)
            .background(color = backgroundColor, shape = MaterialTheme.shapes.small)
            .border(width = 1.dp, color = Color.Black, shape = MaterialTheme.shapes.small)
            .padding(horizontal = 2.dp)
            .clickable(onClick = onSelectDay)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy((-4).dp)
        ) {
            Text(
                text = day.dayOfMonth.toString(),
                color = textColor,
                textAlign = TextAlign.Center,
                style = numberTextStyle,
                modifier = Modifier.padding(top = 6.dp),
            )
            Text(
                text = when (day.dayOfWeek) {
                    DayOfWeek.MONDAY -> stringResource(R.string.mon)
                    DayOfWeek.TUESDAY -> stringResource(R.string.tue)
                    DayOfWeek.WEDNESDAY -> stringResource(R.string.wed)
                    DayOfWeek.THURSDAY -> stringResource(R.string.thu)
                    DayOfWeek.FRIDAY -> stringResource(R.string.fri)
                    DayOfWeek.SATURDAY -> stringResource(R.string.sat)
                    DayOfWeek.SUNDAY -> stringResource(R.string.sun)
                    else -> ""
                },
                color = textColor,
                textAlign = TextAlign.Center,
                style = dayTextStyle,
                fontWeight = dayFontWeight
            )
        }
    }
}
