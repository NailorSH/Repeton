package com.nailorsh.repeton.features.newlesson.presentation.ui.components.first

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.nailorsh.repeton.R
import com.nailorsh.repeton.core.util.getDisabledInteractiveOutlinedTextFieldColors
import java.time.LocalTime
import java.time.format.DateTimeFormatter.ofLocalizedTime
import java.time.format.FormatStyle


@Composable
fun EndTimeTextField(
    time: LocalTime,
    isError: Boolean,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }

    OutlinedTextField(
        isError = isError,
        modifier = Modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { onClick() },
        enabled = false,
        colors = getDisabledInteractiveOutlinedTextFieldColors(isError),
        readOnly = true,
        singleLine = true,
        value = time.format(ofLocalizedTime(FormatStyle.SHORT)),
        onValueChange = {},
        placeholder = { Text(stringResource(R.string.new_lesson_screen_end_time)) },
        label = { Text(stringResource(R.string.new_lesson_screen_end_time)) },
        supportingText = { Text(stringResource(R.string.required_field)) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        leadingIcon = {
            Icon(
                painter = painterResource(R.drawable.clock_icon),
                contentDescription = stringResource(R.string.new_lesson_screen_datepick_icon)
            )
        },

        )
}

