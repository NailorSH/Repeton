package com.nailorsh.repeton.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nailorsh.repeton.R
import com.nailorsh.repeton.data.repositories.FakeRepetonRepository
import com.nailorsh.repeton.domain.CurrentLessonUiState
import com.nailorsh.repeton.domain.RepetonViewModel
import com.nailorsh.repeton.model.Lesson
import com.nailorsh.repeton.ui.components.RepetonButton
import com.nailorsh.repeton.ui.theme.*
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun LessonScreen(
    lessonId: Int,
    viewModel: RepetonViewModel = RepetonViewModel(FakeRepetonRepository()),
    modifier: Modifier = Modifier) {

    LaunchedEffect(lessonId) {
        viewModel.getLesson(lessonId)
    }

    val lessonState = viewModel.currentLessonUiState
    when (lessonState) {
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
        color = ScreenBackground,
        modifier = modifier
            .fillMaxSize()
    ) {

        Surface(
            modifier = modifier
                .padding(horizontal = 28.dp, vertical = 32.dp),
            color = ScreenBackground,
        ) {

            Column {
                Divider(
                    modifier = Modifier
                        .width(290.73.dp)
                        .padding(top = 15.8.dp)
                        .align(Alignment.CenterHorizontally),
                    color = LineColor,
                    thickness = 1.dp,
                )
                Spacer(modifier = Modifier.height(24.dp))
                LessonSubject(
                    lesson.subject, Modifier
                        .border(
                            width = 1.dp,
                            shape = RoundedCornerShape(8.dp),
                            color = Color.Black
                        )
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
                LessonCard(lesson, Modifier.padding(bottom = 16.dp))
                HomeworkCard(lesson.homeworkLink)
                Spacer(modifier = Modifier.height(32.dp))
                AdditionalMaterialsCard(lesson.additionalMaterials)
            }
        }
    }
}

@Composable
fun LessonSubject(subject: String, modifier: Modifier = Modifier) {
    Box(
        modifier
    ) {
        Text(
            text = subject,
            fontSize = 18.sp,
            lineHeight = 30.sp,
            fontWeight = FontWeight.ExtraBold,
            color = SubjectColor
        )
    }
}


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
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = lesson.description.orEmpty(),
                fontSize = 16.sp,
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

@Composable
fun TeacherInfo(teacherName: String, modifier: Modifier = Modifier) {
    Row {
        Icon(
            painter = painterResource(id = R.drawable.teacher_icon),
            contentDescription = stringResource(R.string.teacher_icon_desc),
            modifier = modifier
                .size(24.dp)
                .padding(start = 4.dp, top = 4.dp)
        )
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text(
                text = stringResource(R.string.conduct_lesson),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = teacherName
            )
        }
    }
}

@Composable
fun TimeInfo(startTime: String, endTime: String, modifier: Modifier = Modifier) {
    Row {
        Icon(
            painter = painterResource(id = R.drawable.clock_icon),
            contentDescription = "Time",
            modifier = modifier
                .size(24.dp)
                .padding(start = 4.dp, top = 4.dp)

        )
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text(
                text = stringResource(R.string.date_and_time),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            Text(text = "$startTime - $endTime")
        }
    }
}

@Composable
fun HomeworkCard(hwLink: String?, modifier: Modifier = Modifier) {
    Box(modifier) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = stringResource(R.string.homework),
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 25.sp
                )

            )
            Spacer(modifier = Modifier.height(12.dp))
            RepetonButton(

                buttonColor = HomeWorkButtonColor,

                onClick = {
                    /* TODO Open Link */
                }
            ) {
                Text(
                    text = stringResource(R.string.to_homework_button),
                    style = TextStyle(
                        fontSize = 15.sp,
                        lineHeight = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    ),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                )

            }
        }
    }
}

@Composable
fun AdditionalMaterialsCard(additionalMaterials: String?, modifier: Modifier = Modifier) {
    Box(modifier) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = stringResource(R.string.additional_materials),
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 25.sp
                )

            )
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .background(color = Color.White)
                    .shadow(elevation = 1.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 16.dp)
                    .heightIn(min = 50.dp)


            ) {
                Text(
                    text = additionalMaterials ?: stringResource(R.string.no_additional_materials),
                    style = TextStyle(
                        fontSize = 18.sp,
                        lineHeight = 25.sp,
                    )

                )
            }

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