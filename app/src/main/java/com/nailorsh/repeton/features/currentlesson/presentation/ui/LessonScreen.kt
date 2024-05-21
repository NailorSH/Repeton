package com.nailorsh.repeton.features.currentlesson.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nailorsh.repeton.R
import com.nailorsh.repeton.common.data.models.Id
import com.nailorsh.repeton.common.data.models.lesson.Lesson
import com.nailorsh.repeton.core.ui.theme.LineColor
import com.nailorsh.repeton.features.currentlesson.presentation.ui.components.AdditionalMaterialsCard
import com.nailorsh.repeton.features.currentlesson.presentation.ui.components.HomeworkCard
import com.nailorsh.repeton.features.currentlesson.presentation.ui.components.LessonCard
import com.nailorsh.repeton.features.currentlesson.presentation.ui.components.LessonSubject
import com.nailorsh.repeton.features.currentlesson.presentation.viewmodel.CurrentLessonUiState
import com.nailorsh.repeton.features.currentlesson.presentation.viewmodel.CurrentLessonViewModel

@Composable
fun LessonScreen(
    lessonId: Id,
    onNavigateHomework: () -> Unit,
    viewModel: CurrentLessonViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
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
            LessonContent(lessonState.lesson, onNavigateHomework)
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
fun LessonContent(lesson: Lesson, onNavigateHomework: () -> Unit, modifier: Modifier = Modifier) {

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 28.dp, vertical = 32.dp),
    ) {

        val scrollState = rememberScrollState()
        Column(
            Modifier
                .verticalScroll(scrollState)
        ) {
            HorizontalDivider(
                modifier = Modifier
                    .width(dimensionResource(R.dimen.divider_width))
                    .padding(top = dimensionResource(R.dimen.padding_medium))
                    .align(Alignment.CenterHorizontally),
                color = LineColor,
                thickness = dimensionResource(R.dimen.divider_thickness),
            )
            Spacer(modifier = Modifier.height(24.dp))

            // TODO - передавать locale из настроек
            LessonSubject(
                lesson.subject.name,
                modifier = Modifier.padding(start = 8.dp)
            )
            LessonCard(lesson, Modifier.padding(bottom = dimensionResource(R.dimen.padding_medium)))
            HomeworkCard(lesson.homework, onNavigateHomework)
            Spacer(modifier = Modifier.height(32.dp))
            AdditionalMaterialsCard(lesson.additionalMaterials)
        }

    }
}


//@Preview(
//    showSystemUi = true,
//    showBackground = true
//)
//@Composable
//fun LessonScreenPreview() {
//    RepetonTheme {
//        LessonScreen(lessonId = Id("1"))
//    }
//}