package com.nailorsh.repeton.features.schedule.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .fillMaxSize()
            .padding(top = 6.dp)

    ) {
        if (lessons.isEmpty()) {

            ElevatedCard(
                modifier = modifier
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
        } else
            lessons.forEach { lesson ->
                LessonCard(lesson = lesson, onClick = onLessonClicked)
            }


    }


}