package com.nailorsh.repeton.schedule.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nailorsh.repeton.R
import com.nailorsh.repeton.currentlesson.data.model.Lesson

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
        if (lessons.isEmpty()) {
            Box(
                modifier = Modifier
                    .padding(top = 21.dp)
                    .width(dimensionResource(R.dimen.schedule_screen_button_width))
                    .height(dimensionResource(R.dimen.schedule_screen_lesson_size))
                    .background(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = MaterialTheme.shapes.medium
                    )
            ) {
                Text(
                    text = stringResource(R.string.lessons_not_found),
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .padding(horizontal = 13.dp, vertical = 16.dp)
                )
            }
        } else {
            LazyColumn {
                items(lessons.size) {
                    LessonCard(
                        lesson = lessons[it],
                        onClick = onLessonClicked
                    )
                }

            }
        }

    }

}