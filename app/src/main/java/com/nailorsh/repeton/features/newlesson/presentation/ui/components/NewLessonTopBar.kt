package com.nailorsh.repeton.features.newlesson.presentation.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nailorsh.repeton.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewLessonTopBar(
    onNavigateBack : () -> Unit,
    actions :  @Composable() (RowScope.() -> Unit) = {},
) {
    TopAppBar(
        title = { Text(stringResource(id = R.string.new_lesson_screen_headline)) },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ),
        actions = actions,
        modifier = Modifier.shadow(elevation = 1.dp)
    )
}