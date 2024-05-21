package com.nailorsh.repeton.features.homework.presentation.ui.components

import androidx.compose.runtime.Composable
import com.nailorsh.repeton.core.ui.components.LessonTopBar

@Composable
fun HomeworkTopBar(
    title : String,
    onNavigateBack : () -> Unit
) {
    LessonTopBar(onNavigateBack = onNavigateBack, title = title)
}