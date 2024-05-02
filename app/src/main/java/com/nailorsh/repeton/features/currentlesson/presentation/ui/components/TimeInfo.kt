package com.nailorsh.repeton.features.currentlesson.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nailorsh.repeton.R

@Composable
fun TimeInfo(startTime: String, endTime: String, modifier: Modifier = Modifier) {
    Row {
        Icon(
            painter = painterResource(id = R.drawable.ic_clock_icon),
            contentDescription = stringResource(R.string.lessonscreen_timeinfo_descrpition),
            modifier = modifier
                .size(24.dp)
                .padding(start = 4.dp, top = 4.dp)

        )
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text(
                text = stringResource(R.string.date_and_time),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(text = "$startTime - $endTime")
        }
    }
}