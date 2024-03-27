package com.nailorsh.repeton.currentlesson.presentation.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nailorsh.repeton.currentlesson.data.model.Lesson
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
                text = lesson.title,
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
