package com.nailorsh.repeton.features.newlesson.presentation.ui.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.nailorsh.repeton.R

@Composable
fun TopicTextField(
    topic: String,
    onTopicChange: (String) -> Unit,
) {

    OutlinedTextField(
        singleLine = true,
        value = topic,
        onValueChange = {
            onTopicChange(it)
        },
        label = { Text(stringResource(R.string.new_lesson_screen_topic_label)) },
        placeholder = { Text(stringResource(R.string.new_lesson_screen_topic_placeholder)) },
        supportingText = { Text(stringResource(R.string.required_field)) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
    )


}

