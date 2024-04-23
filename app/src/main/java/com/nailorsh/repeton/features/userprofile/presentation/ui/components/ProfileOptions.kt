package com.nailorsh.repeton.features.userprofile.presentation.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.nailorsh.repeton.features.userprofile.presentation.viewmodel.Options

@Composable
fun ProfileOptions(
    optionsList: List<Options>,
    onOptionClicked : (Options) -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        LazyColumn {
            itemsIndexed(optionsList) { index, item ->
                ProfileMenuItem(
                    icon = item.icon,
                    text = item.text,
                    onPressCallback = { onOptionClicked(item) },
                    trailingItem = { }
                )
                if (index != optionsList.size - 1) {
                    HorizontalDivider()
                }
            }
        }

    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileOptionsPreview() {
    ProfileOptions(listOf(), {})
}