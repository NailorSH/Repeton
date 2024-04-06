package com.nailorsh.repeton.features.newlesson.presentation.ui.components

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.nailorsh.repeton.R
import java.time.LocalDate


@Composable
fun DateTextField(
    date: LocalDate,
    firstSet : Boolean,
    isError : Boolean,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    Log.d("DATE", firstSet.toString())
    OutlinedTextField(
        isError = isError,
        modifier = Modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { onClick() },
        enabled = false,
        colors = OutlinedTextFieldDefaults.colors(
            disabledTextColor = MaterialTheme.colorScheme.onSurface,
            disabledBorderColor = MaterialTheme.colorScheme.outline,
            disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledSupportingTextColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        readOnly = true,
        singleLine = true,
        value = if (firstSet) "" else date.toString(),
        onValueChange = {},
        label = { Text(stringResource(R.string.new_lesson_screen_date_label)) },
        placeholder = { Text(stringResource(R.string.new_lesson_screen_date_label)) },
        supportingText = { Text(stringResource(R.string.required_field)) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        leadingIcon = {
            Icon(
                painter = painterResource(R.drawable.ic_calendar_w400),
                contentDescription = stringResource(R.string.new_lesson_screen_datepick_icon)
            )
        },

    )
}

