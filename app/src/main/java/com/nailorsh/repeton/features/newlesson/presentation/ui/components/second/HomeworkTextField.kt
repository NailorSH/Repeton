package com.nailorsh.repeton.features.newlesson.presentation.ui.components.second

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.nailorsh.repeton.R

@Composable
fun HomeworkTextField(
    homeworkText: String,
    onHomeworkTextChange: (String) -> Unit,
    onClickAttachFile: () -> Unit,
    onClickAddAPhoto: () -> Unit,
) {
    var lines by remember { mutableIntStateOf(3) }
    Box() {
        OutlinedTextField(
            minLines = 3,
            value = homeworkText,
            onValueChange = {
                onHomeworkTextChange(it)
            },
            label = { Text(stringResource(R.string.new_lesson_screen_homework_label)) },
            placeholder = { Text(stringResource(R.string.new_lesson_screen_homework_placeholder)) },
            supportingText = { Text(stringResource(R.string.optional_field)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
        )
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy((-8).dp),
            modifier = Modifier.align(Alignment.BottomEnd).padding(bottom = 16.dp)
        ) {
            IconButton(onClick = onClickAttachFile) {
                Icon(
                    painter = painterResource(R.drawable.ic_attach_file_w300),
                    contentDescription = null
                )
            }
            IconButton(onClick = onClickAddAPhoto) {
                Icon(
                    painter = painterResource(R.drawable.ic_add_a_photo_w300),
                    contentDescription = null
                )
            }
        }
    }



}

