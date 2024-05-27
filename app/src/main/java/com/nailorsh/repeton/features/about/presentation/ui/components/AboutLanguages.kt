package com.nailorsh.repeton.features.about.presentation.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nailorsh.repeton.R
import com.nailorsh.repeton.common.data.models.Id
import com.nailorsh.repeton.common.data.models.language.Language
import com.nailorsh.repeton.common.data.models.language.LanguageLevel
import com.nailorsh.repeton.common.data.models.language.LanguageWithLevel
import com.nailorsh.repeton.core.util.getDisabledInteractiveOutlinedTextFieldColors
import com.nailorsh.repeton.features.about.data.model.LanguageItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutLanguages(
    searchQuery: String,
    languages: List<LanguageItem>,
    addedLanguageWithLevels: List<LanguageWithLevel>?,
    languageLevels: List<LanguageLevel>,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    onAddLanguage: (LanguageItem) -> Unit,
    onRemoveLanguage: (LanguageWithLevel) -> Unit,
    updateLanguageLevel: (LanguageWithLevel, LanguageLevel) -> Unit,
    modifier: Modifier = Modifier
) {
    var active by rememberSaveable {
        mutableStateOf(false)
    }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp), modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(painter = painterResource(id = R.drawable.ic_language), contentDescription = null)
            Text(
                text = stringResource(R.string.about_screen_languages),
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.weight(1f)
            )
        }
        DockedSearchBar(
            query = searchQuery,
            onQueryChange = onQueryChange,
            onSearch = onSearch,
            active = active,
            onActiveChange = { active = it },
            modifier = Modifier.imePadding(),
            trailingIcon = {
                if (active) {
                    IconButton(onClick = { onQueryChange("") }) {
                        Icon(imageVector = Icons.Default.Clear, contentDescription = null)
                    }
                }
            },
            leadingIcon = {
                if (active) IconButton(onClick = { active = false }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null
                    )
                } else Icon(imageVector = Icons.Default.Search, contentDescription = null)
            },
            placeholder = { Text("Поиск языка") }
        ) {
            LazyColumn(
            ) {
                items(languages) { language ->
                    ListItem(headlineContent = {
                        Text(
                            text = language.name,
                            style = MaterialTheme.typography.titleSmall
                        )
                    }, modifier = Modifier.clickable {
                        onAddLanguage(language)
                        active = false
                    })
                }

            }
        }
        if (addedLanguageWithLevels != null)
            Column {
                addedLanguageWithLevels.forEach { languageWithLevel ->
                    var languageDropDownMenu by rememberSaveable {
                        mutableStateOf(false)
                    }
                    ListItem(
                        headlineContent = {
                            Text(
                                text = languageWithLevel.language.name,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1
                            )
                        },
                        leadingContent = {
                            IconButton(onClick = { onRemoveLanguage(languageWithLevel) }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_remove),
                                    contentDescription = null
                                )
                            }
                        },
                        supportingContent = {
                            Text(
                                stringResource(R.string.about_screen_skill_level),
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1
                            )
                        },
                        trailingContent = {
                            ExposedDropdownMenuBox(
                                expanded = languageDropDownMenu,
                                onExpandedChange = { languageDropDownMenu = it },
                                modifier = Modifier
                                    .widthIn(min = 96.dp, max = 124.dp)
                                    .height(48.dp)
                            ) {
                                TextField(
                                    enabled = true,
                                    readOnly = true,
                                    value = "",
                                    prefix = {
                                        Text(text = if (languageWithLevel.level == LanguageLevel.OTHER) "" else languageWithLevel.level.toString(), style = MaterialTheme.typography.titleSmall)
                                    },
                                    textStyle = MaterialTheme.typography.labelMedium,
                                    onValueChange = {},
                                    colors = getDisabledInteractiveOutlinedTextFieldColors(error = false),
                                    trailingIcon = {
                                        AnimatedVisibility(
                                            visible = !languageDropDownMenu,
                                            enter = expandHorizontally(),
                                            exit = shrinkHorizontally()
                                        ) {
                                            IconButton(
                                                onClick = { languageDropDownMenu = true }
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.KeyboardArrowDown,
                                                    contentDescription = stringResource(R.string.new_lesson_screen_show_subject_list)
                                                )
                                            }
                                        }
                                        AnimatedVisibility(
                                            visible = languageDropDownMenu,
                                            enter = expandHorizontally(),
                                            exit = shrinkHorizontally()
                                        ) {
                                            IconButton(
                                                onClick = { languageDropDownMenu = false }
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.KeyboardArrowUp,
                                                    contentDescription = stringResource(R.string.new_lesson_screen_hide_subject_list)
                                                )
                                            }
                                        }

                                    },
                                    modifier = Modifier.menuAnchor()
                                )
                                DropdownMenu(
                                    expanded = languageDropDownMenu,
                                    onDismissRequest = { languageDropDownMenu = false }) {
                                    languageLevels.forEach { languageLevel ->
                                        DropdownMenuItem(
                                            text = { Text(text = languageLevel.toString()) },
                                            onClick = {
                                                updateLanguageLevel(
                                                    languageWithLevel,
                                                    languageLevel
                                                )
                                                languageDropDownMenu = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    )
                }
            }
    }
}


@Preview(showBackground = true, showSystemUi = false)
@Composable
fun AboutLanguagesPreview() {
    AboutLanguages(
        searchQuery = "",
        languages = emptyList(),
        addedLanguageWithLevels = listOf(
            LanguageWithLevel(
                Language(Id(""), name = "Английский"),
                level = LanguageLevel.B2
            )
        ),
        languageLevels = emptyList(),
        onQueryChange = {},
        onSearch = {},
        onAddLanguage = {},
        onRemoveLanguage = {},
        updateLanguageLevel = { _, _ -> }
    )
}