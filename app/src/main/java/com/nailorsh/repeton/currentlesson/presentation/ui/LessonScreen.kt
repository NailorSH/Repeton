package com.nailorsh.repeton.currentlesson.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nailorsh.repeton.R
import com.nailorsh.repeton.core.ui.components.RepetonButton
import com.nailorsh.repeton.core.ui.theme.HomeWorkButtonColor
import com.nailorsh.repeton.core.ui.theme.LineColor
import com.nailorsh.repeton.core.ui.theme.RepetonTheme
import com.nailorsh.repeton.core.ui.theme.ScreenBackground
import com.nailorsh.repeton.core.ui.theme.SubjectColor
import com.nailorsh.repeton.currentlesson.data.model.Lesson
import com.nailorsh.repeton.currentlesson.presentation.ui.components.AdditionalMaterialsCard
import com.nailorsh.repeton.currentlesson.presentation.ui.components.HomeworkCard
import com.nailorsh.repeton.currentlesson.presentation.ui.components.LessonCard
import com.nailorsh.repeton.currentlesson.presentation.ui.components.LessonSubject
import com.nailorsh.repeton.currentlesson.presentation.viewmodel.CurrentLessonUiState
import com.nailorsh.repeton.currentlesson.presentation.viewmodel.CurrentLessonViewModel
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun LessonScreen(
    lessonId: Int,
    viewModel: CurrentLessonViewModel = viewModel(),
    modifier: Modifier = Modifier
) {

    LaunchedEffect(lessonId) {
        viewModel.getLesson(lessonId)
    }


    when (val lessonState = viewModel.currentLessonUiState) {
        is CurrentLessonUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        is CurrentLessonUiState.Success -> {
            LessonContent(lessonState.lesson)
        }

        CurrentLessonUiState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = stringResource(R.string.error_lesson_loading),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun LessonContent(lesson: Lesson, modifier: Modifier = Modifier) {

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 28.dp, vertical = 32.dp),
    ) {


        Column {
            HorizontalDivider(
                modifier = Modifier
                    .width(dimensionResource(R.dimen.divider_width))
                    .padding(top = dimensionResource(R.dimen.padding_medium))
                    .align(Alignment.CenterHorizontally),
                color = LineColor,
                thickness = dimensionResource(R.dimen.divider_thickness),
            )
            Spacer(modifier = Modifier.height(24.dp))
            LessonSubject(
                lesson.subject,
                modifier = Modifier.padding(start = 8.dp)
            )
            LessonCard(lesson, Modifier.padding(bottom = dimensionResource(R.dimen.padding_medium)))
            HomeworkCard(lesson.homeworkLink)
            Spacer(modifier = Modifier.height(32.dp))
            AdditionalMaterialsCard(lesson.additionalMaterials)
        }

    }
}






@Preview(
    showSystemUi = true,
    showBackground = true
)
@Composable
fun LessonScreenPreview() {
    RepetonTheme {
        LessonScreen(0)
    }
}