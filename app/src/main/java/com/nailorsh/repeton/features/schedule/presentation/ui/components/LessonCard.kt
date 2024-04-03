package com.nailorsh.repeton.features.schedule.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nailorsh.repeton.R
import com.nailorsh.repeton.common.data.models.Lesson
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun LessonCard(lesson: Lesson, onClick: (Lesson) -> Unit, modifier: Modifier = Modifier) {

    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
        ),
        modifier = Modifier
            .padding(top = 10.dp, bottom = 10.dp)
            .width(dimensionResource(R.dimen.schedule_screen_button_width))
            .height(dimensionResource(R.dimen.schedule_screen_lesson_size))

            .clickable { onClick(lesson) }
    )
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 13.dp)

        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            )
            {
                Text(
                    text = lesson.subject,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    style = MaterialTheme.typography.headlineSmall,

                    )


                // Время начала и конца занятия
                val startTimeCutted =
                    lesson.startTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT))
                        .substringAfter(", ")
                val endTimeCutted =
                    lesson.endTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT))
                        .substringAfter(", ")
                Text(
                    text = "$startTimeCutted - $endTimeCutted",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Left,
                    modifier = Modifier
                        .padding(start = 4.dp)
                )
            }
            Text(
                text = lesson.teacherName,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = MaterialTheme.typography.bodyMedium,
                letterSpacing = 0.25.sp,
            )
            Text(
                text = lesson.title,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.6f),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}