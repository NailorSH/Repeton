package com.nailorsh.repeton.features.schedule.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nailorsh.repeton.R
import com.nailorsh.repeton.common.data.models.lesson.Lesson

@Composable
fun LessonsList(
    onLessonClicked: (Lesson) -> Unit,
    lessons: List<Lesson>,
    modifier: Modifier = Modifier
) {
    Spacer(
        modifier = Modifier
            .height(10.dp)
    )
    Column(
        modifier = modifier
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(top = 6.dp)
        ) {
        if (lessons.isEmpty()) {
            item {
                ElevatedCard(
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .width(dimensionResource(R.dimen.schedule_screen_button_width))
                        .height(dimensionResource(R.dimen.schedule_screen_lesson_size))
                ) {
                    Text(
                        text = stringResource(R.string.lessons_not_found),
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier
                            .padding(horizontal = 13.dp, vertical = 16.dp)
                    )
                }
            }
        } else {
                items(items = lessons) { lesson ->
                    LessonCard(lesson = lesson, onClick = onLessonClicked)
                }
            }
        }

    }

}