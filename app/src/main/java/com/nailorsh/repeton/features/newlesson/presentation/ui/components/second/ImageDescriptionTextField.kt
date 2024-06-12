package com.nailorsh.repeton.features.newlesson.presentation.ui.components.second

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.nailorsh.repeton.R

@Composable
fun ImageDescriptionTextField(
    description : String,
    onDescriptionChange : (String) -> Unit,
    modifier : Modifier = Modifier
) {
    TextField(
        singleLine = false,
        value = description,
        onValueChange = {
            onDescriptionChange(it)
        },
        label = { Text(stringResource(R.string.new_lesson_screen_image_description_label)) },
        placeholder = { Text(stringResource(R.string.new_lesson_screen_image_description_placeholder)) },
        supportingText = { Text(stringResource(R.string.optional_field)) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        modifier = modifier
    )
}