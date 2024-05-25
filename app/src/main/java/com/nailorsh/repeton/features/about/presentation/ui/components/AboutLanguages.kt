package com.nailorsh.repeton.features.about.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nailorsh.repeton.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutLanguages(
    searchQuery : String,
    onQueryChange : (String) -> Unit,
    onSearch : (String) -> Unit,
    searchIsActive : Boolean,
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
            onActiveChange = { active = !it }
        ) {
            
        }
    }
}


@Preview(showBackground = true, showSystemUi = false)
@Composable
fun AboutLanguagesPreview() {
    AboutLanguages(
        searchQuery = "",
        onQueryChange = {},
        onSearch = {},
        searchIsActive = true,
    )
}