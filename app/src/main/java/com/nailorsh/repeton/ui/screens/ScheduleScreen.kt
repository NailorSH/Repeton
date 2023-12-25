package com.nailorsh.repeton.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposableTarget
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nailorsh.repeton.R
import com.nailorsh.repeton.data.LessonSource
import com.nailorsh.repeton.model.Lesson
import com.nailorsh.repeton.ui.theme.AmbientColor
import com.nailorsh.repeton.ui.theme.RepetonTheme
import com.nailorsh.repeton.ui.theme.SpotColor
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScheduleScreen(lesson: Lesson) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(android.graphics.Color.parseColor("#EFEFEF")))
    ) {
        Divider(
            modifier = Modifier
                .padding(top = 47.6.dp)
                .width(290.73.dp)
                .align(Alignment.CenterHorizontally),
            color = Color(android.graphics.Color.parseColor("#BAB3B3")),
            thickness = 1.dp,

            )
        Box(
            modifier = Modifier
                .padding(top = 16.4.dp)
                .width(294.dp)
                .height(63.dp)
                .background(color = Color.White, shape = RoundedCornerShape(size = 16.dp))
                .align(Alignment.CenterHorizontally)

        )
        {
            Text(
                text = "Календарь",
                color = Color.Black,
                fontSize = 28.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }
        Row(
            modifier = Modifier
                .padding(top = 16.dp)
                .width(294.dp)
                .align(Alignment.CenterHorizontally),
            horizontalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            ObjectInRow("1", "пн")
            ObjectInRow("2", "вт")
            ObjectInRow("3", "ср")
            ObjectInRow("4", "чт")
            ObjectInRow("5", "пт")
            ObjectInRow("6", "сб")
            ObjectInRow("7", "вс")
        }
        Divider(
            modifier = Modifier
                .padding(top = 22.dp)
                .width(290.73.dp)
                .align(Alignment.CenterHorizontally),
            color = Color(android.graphics.Color.parseColor("#BAB3B3")),
            thickness = 1.dp,

            )
        Column(
            modifier = Modifier
                .width(296.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            val lessons = LessonSource().loadLessons()
            LessonBox(lessons[0])
            LessonBox(lessons[1])
            LessonBox(lessons[2])
        }
        Button(
            onClick = { },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 30.dp)
                .width(298.dp)
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(android.graphics.Color.parseColor("#4B9E58")),
                contentColor = Color.Black
            )
        ) {
            Text(text = "Добавить занятие",
                color = Color.White
            )
        }
    }
}
@Composable
fun ObjectInRow(number: String, day: String) {
    Box(
        modifier = Modifier
            .width(36.dp)
            .height(48.dp)
            .background(
                color = Color(android.graphics.Color.parseColor("#EFEFEF")),
                shape = RoundedCornerShape(size = 8.dp)
            )
            .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(size = 8.dp))
    )
    {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(-4.dp)
        ) {
            Text(
                text = number,
                color = Color.Black,
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 6.dp)
            )
            Text(
                text = day,
                color = Color.Black,
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                fontWeight = FontWeight.Light,
            )
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LessonBox(lesson: Lesson) {
    Box(
        modifier = Modifier
            .padding(top = 21.dp)
            .width(296.dp)
            .height(95.dp)
            .background(color = Color.White, shape = RoundedCornerShape(size = 16.dp))
    )
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 13.dp,)
        ) {
            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween)
                {
                Text(
                    text = lesson.subject,
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,

                    )
                    //Обрезала дату, при выборе конкретного дня она лишняя
                    val startTimeCutted = lesson.startTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)).substringAfter(", ")
                    val endTimeCutted = lesson.endTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)).substringAfter(", ")
                Text(
                    text = "$startTimeCutted - $endTimeCutted",
                    fontSize = 14.sp,
                    letterSpacing = 0.sp,
                    textAlign = TextAlign.Left,
                    fontWeight = FontWeight.Medium)
            }
            Text(
                text = lesson.teacherName,
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            )
            Text(
                text = lesson.title,
                style = LocalTextStyle.current.copy(
                    lineHeight = 13.sp
                ),
                color = Color.Black.copy(alpha = 0.6f),
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Preview(
    showSystemUi = true,
    showBackground = true
)
@Composable
fun ScheduleScreenPreview() {
    RepetonTheme {
        ScheduleScreen(LessonSource().loadLessons()[0])
    }
}