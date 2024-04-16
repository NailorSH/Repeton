package com.nailorsh.repeton.features.tutorprofile.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.nailorsh.repeton.R

@Composable
fun LanguageSkills(tutorLanguages: Map<String, String>) {
    Text(
        text = stringResource(R.string.my_languages),
        style = MaterialTheme.typography.titleLarge,
    )

    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))

    Column {
        tutorLanguages.forEach { (language, level) ->
            LanguageItem(name = language, level = level)
        }
    }
}