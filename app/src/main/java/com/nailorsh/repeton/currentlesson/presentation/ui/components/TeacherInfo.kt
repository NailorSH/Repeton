package com.nailorsh.repeton.currentlesson.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nailorsh.repeton.R

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
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = teacherName
            )
        }
    }
}