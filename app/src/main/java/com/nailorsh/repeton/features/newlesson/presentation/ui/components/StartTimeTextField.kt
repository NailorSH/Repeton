package com.nailorsh.repeton.features.newlesson.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.nailorsh.repeton.R
import com.nailorsh.repeton.features.newlesson.presentation.ui.getColorsForTextField
import java.lang.Error
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatter.ofLocalizedDateTime
import java.time.format.DateTimeFormatter.ofLocalizedTime
import java.time.format.FormatStyle


@Composable
fun StartTimeTextField(
    time: LocalTime,
    firstSet: Boolean,
    onClick: () -> Unit,
    isError: Boolean,
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
        colors = getColorsForTextField(isError),
        readOnly = true,
        singleLine = true,
        value = if (firstSet) "" else time.format(ofLocalizedTime(FormatStyle.SHORT)),
        onValueChange = {},
        placeholder = { Text(stringResource(R.string.new_lesson_screen_start_time_label)) },
        label = { Text(stringResource(R.string.new_lesson_screen_start_time_label)) },
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


