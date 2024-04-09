package com.nailorsh.repeton.features.newlesson.presentation.ui.components.second

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.nailorsh.repeton.R

@Composable
fun AdditionalMaterialsTextField(
    additionalMaterials : String,
    onAdditionalMaterialsChange : (String) -> Unit
) {

    OutlinedTextField(
        singleLine = false,
        value = additionalMaterials,
        onValueChange = {
            onAdditionalMaterialsChange(it)
        },
        label = { Text(stringResource(R.string.new_lesson_screen_additional_materials_label)) },
        placeholder = { Text(stringResource(R.string.new_lesson_screen_additional_materials_placeholder)) },
        supportingText = { Text(stringResource(R.string.optional_field)) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
    )
}