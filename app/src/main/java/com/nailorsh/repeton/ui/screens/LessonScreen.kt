package com.nailorsh.repeton.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nailorsh.repeton.R
import com.nailorsh.repeton.data.LessonSource
import com.nailorsh.repeton.model.Lesson
import com.nailorsh.repeton.ui.theme.RepetonTheme
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun LessonScreen(lessonId: Int, modifier: Modifier = Modifier) {
    val lesson = LessonSource.loadLessons()[lessonId]

    Surface(modifier.padding(horizontal = 16.dp, vertical = 32.dp)) {
        Column {
            LessonSubject(lesson.subject, Modifier
                .border(
                    width = 1.dp,
                    shape = RoundedCornerShape(8.dp),
                    color = Color.Black
                )
                .padding(horizontal = 16.dp, vertical = 8.dp))
            LessonCard(lesson, Modifier.padding(vertical = 16.dp))
            HomeworkCard(lesson.homeworkLink)
            AdditionalMaterialsCard(lesson.additionalMaterials)
        }
    }
}

@Composable
fun LessonSubject(subject: String, modifier: Modifier = Modifier) {
    Surface(
        modifier

    ) {
        Text(
            text = subject,
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(R.color.subject_color)
        )
    }
}


@Composable
fun LessonCard(lesson: Lesson, modifier: Modifier = Modifier) {
    Card(
        modifier
            .padding(vertical = 16.dp)
            .background(Color.White)
        ,
    ) {
        Column(Modifier
            .padding(8.dp)
        ) {
            Text(
                text = lesson.title,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = lesson.description.orEmpty(),
                fontSize = 16.sp,
            )
            Spacer(modifier = Modifier.height(24.dp))
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
            contentDescription = "Teacher",
            modifier = modifier
                .size(24.dp)
                .padding(start = 4.dp, top = 4.dp)
        )
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text(
                text = "Занятие ведёт",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(text = teacherName)
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
            Text(text = "Дата и время занятия")
            Text(text = "$startTime - $endTime")
        }
    }
}

@Composable
fun HomeworkCard(hwLink : String?, modifier: Modifier = Modifier) {
    Card(modifier) {

    }
}

@Composable
fun AdditionalMaterialsCard(additionalMaterials: String?, modifier: Modifier = Modifier) {
    Card(modifier) {

    }
}


@Preview(
    showSystemUi = true,
    showBackground = true
)
@Composable
fun LessonScreenPreview() {
    RepetonTheme {
        LessonScreen(lessonId = 0)
    }
}