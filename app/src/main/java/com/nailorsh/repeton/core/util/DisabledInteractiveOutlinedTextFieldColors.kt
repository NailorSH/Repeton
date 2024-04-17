package com.nailorsh.repeton.core.util

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable


// Возвращает disabled OutlinedTextField'у его enabled цвета. Окрашивает textField в Error-colors,
// если передан флаг error = true
@Composable
fun getDisabledInteractiveOutlinedTextFieldColors(error: Boolean): TextFieldColors {
    if (!error) {
        return OutlinedTextFieldDefaults.colors(
            disabledTextColor = MaterialTheme.colorScheme.onSurface,
            disabledBorderColor = MaterialTheme.colorScheme.outline,
            disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledSupportingTextColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    } else {
        return OutlinedTextFieldDefaults.colors(
            disabledTextColor = MaterialTheme.colorScheme.error,
            disabledBorderColor = MaterialTheme.colorScheme.error,
            disabledPlaceholderColor = MaterialTheme.colorScheme.error,
            disabledLabelColor = MaterialTheme.colorScheme.error,
            disabledLeadingIconColor = MaterialTheme.colorScheme.error,
            disabledTrailingIconColor = MaterialTheme.colorScheme.error,
            disabledSupportingTextColor = MaterialTheme.colorScheme.error
        )
    }
}