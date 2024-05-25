package com.nailorsh.repeton.features.about.presentation.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nailorsh.repeton.R
import com.nailorsh.repeton.core.util.getDisabledInteractiveOutlinedTextFieldColors
import com.nailorsh.repeton.features.about.data.model.EducationItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutEducation(
    education: String?,
    specialization: String?,
    educationsList: List<EducationItem>,
    onEducationChange: (EducationItem) -> Unit,
    onSpecializationChange: (String) -> Unit,
    isExpanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp), modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(painter = painterResource(id = R.drawable.ic_lessons), contentDescription = null)
            Text(
                text = stringResource(R.string.about_screen_education),
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.weight(1f)
            )
        }
        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = onExpandedChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
        ) {
            OutlinedTextField(
                value = education ?: "",
                onValueChange = { },
                enabled = false,
                singleLine = true,
                colors = getDisabledInteractiveOutlinedTextFieldColors(false),
                trailingIcon = {
                    AnimatedVisibility(
                        visible = !isExpanded,
                        enter = expandHorizontally(),
                        exit = shrinkHorizontally()
                    ) {
                        IconButton(
                            onClick = { onExpandedChange(true) }
                        ) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = stringResource(R.string.new_lesson_screen_show_subject_list)
                            )
                        }
                    }
                    AnimatedVisibility(
                        visible = isExpanded,
                        enter = expandHorizontally(),
                        exit = shrinkHorizontally()
                    ) {
                        IconButton(
                            onClick = { onExpandedChange(false) }
                        ) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowUp,
                                contentDescription = stringResource(R.string.new_lesson_screen_hide_subject_list)
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { onExpandedChange(false) },
                modifier = Modifier.fillMaxWidth()) {
                educationsList.forEach {
                    DropdownMenuItem(text = { it.name }, onClick = { onEducationChange(it) })
                }
            }
        }
        if (education != null) {
            TextField(
                value = specialization ?: "",
                onValueChange = onSpecializationChange,
                supportingText = { Text(stringResource(id = R.string.required_field)) },
                label = { Text(stringResource(R.string.about_screen_specialization_label)) },
                placeholder = { Text(stringResource(R.string.about_screen_specialization_placeholder)) },
                trailingIcon = {
                    IconButton(onClick = { onSpecializationChange("") }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_cancel_w400),
                            contentDescription = null
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp)
            )
        }
    }
}