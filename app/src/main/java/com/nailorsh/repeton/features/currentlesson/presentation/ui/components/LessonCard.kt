package com.nailorsh.repeton.features.currentlesson.presentation.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nailorsh.repeton.common.data.models.lesson.Lesson
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun LessonCard(lesson: Lesson, modifier: Modifier = Modifier) {
    Box(
        modifier
            .padding(vertical = 12.dp)
    ) {
        Column(
            Modifier
                .padding(8.dp)
        ) {
            Text(
                text = lesson.topic,
                style = MaterialTheme.typography.displaySmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = lesson.description.orEmpty(),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(48.dp))
            TeacherInfo(lesson.teacherName)
            Spacer(modifier = Modifier.height(16.dp))
            TimeInfo(
                lesson.startTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)),
                lesson.endTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT))
            )
        }
    }
}
