package com.nailorsh.repeton.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.nailorsh.repeton.R
import com.nailorsh.repeton.data.FakeLessonSource
import com.nailorsh.repeton.model.Lesson
import com.nailorsh.repeton.ui.components.RepetonButton
import com.nailorsh.repeton.ui.theme.HomeWorkButtonColor
import com.nailorsh.repeton.ui.theme.RepetonTheme
import com.nailorsh.repeton.ui.theme.ScreenBackground
import com.nailorsh.repeton.ui.theme.SubjectColor
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun LessonScreen(lessonId: Int, modifier: Modifier = Modifier) {
    val lesson = FakeLessonSource.loadLessons()[lessonId]
    Surface(
        color = ScreenBackground,
    ) {
        Surface(
            modifier = modifier
                .padding(horizontal = 16.dp, vertical = 32.dp),
            color = ScreenBackground,
            ) {
            Column {
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
            .padding(vertical = 16.dp)
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