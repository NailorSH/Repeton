package com.nailorsh.repeton.features.newlesson.presentation.ui.components.second

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.nailorsh.repeton.R

@Composable
fun HomeworkTextField(
    homeworkText: String,
    onHomeworkTextChange: (String) -> Unit,
    onClickAttachFile: () -> Unit,
    onClickCameraRequest: () -> Unit,
) {
    OutlinedTextField(
        minLines = 2,
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
        trailingIcon = {
            Row(verticalAlignment = Alignment.Bottom) {
                IconButton(onClick = onClickAttachFile) {
                    Icon(
                        painter = painterResource(R.drawable.ic_attach_file_w300),
                        contentDescription = null
                    )
                }
                IconButton(onClick = onClickCameraRequest) {
                    Icon(
                        painter = painterResource(R.drawable.ic_image),
                        contentDescription = null
                    )
                }
            }
        },
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeworkTextFieldPreview() {
    HomeworkTextField(
        homeworkText = "Текст",
        onHomeworkTextChange = { },
        onClickAttachFile = { /*TODO*/ }) {

    }
}