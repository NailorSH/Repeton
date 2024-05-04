package com.nailorsh.repeton.features.schedule.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nailorsh.repeton.R
import com.nailorsh.repeton.common.data.models.lesson.Lesson
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun LessonCard(lesson: Lesson, onClick: (Lesson) -> Unit, modifier: Modifier = Modifier) {

    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        colors = CardDefaults.elevatedCardColors(),
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
                .padding(start = 12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            )
            {
                Text(
                    text = lesson.subject.subjectName,
                    style = MaterialTheme.typography.headlineSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(0.65f),
                )

                Spacer(
                    Modifier.weight(0.05f)
                )
                // Время начала и конца занятия
                val startTimeCutted =
                    lesson.startTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT))
                        .substringAfter(", ")
                val endTimeCutted =
                    lesson.endTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT))
                        .substringAfter(", ")
                Text(
                    text = "$startTimeCutted -\n$endTimeCutted",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Left,
                    modifier = Modifier
                        .padding(start = 4.dp, top = 4.dp)
                        .weight(0.3f)
                )
            }
            Text(
                text = lesson.teacherName,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = MaterialTheme.typography.bodyMedium,
                letterSpacing = 0.25.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = lesson.topic,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.6f),
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}