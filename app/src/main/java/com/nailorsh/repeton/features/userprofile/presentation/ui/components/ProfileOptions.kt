package com.nailorsh.repeton.features.userprofile.presentation.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nailorsh.repeton.features.userprofile.data.*

@Composable
fun ProfileOptions(
    optionsList: List<Options>,
    onOptionClicked : (Options) -> Unit,
    modifier : Modifier = Modifier
) {
    ElevatedCard(
        shape = RectangleShape,
        modifier = modifier
            .fillMaxWidth()

    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxHeight()
        ) {
            optionsList.forEachIndexed { index, item ->
                ProfileMenuItem(
                    icon = item.icon,
                    text = item.text,
                    onPressCallback = { onOptionClicked(item) },
                    trailingItem = when(val currentItem = item.trailingItem) {
                        TrailingContentType.Empty -> TrailingContent.Empty
                        is TrailingContentType.HomeworkBadge -> HomeworkBadge(currentItem.count)
                        is TrailingContentType.ThemeSwitcher -> ThemeSwitcher(currentItem.isEnabled, currentItem.onSwitchCallback)
                    }
                )
                if (index != optionsList.size - 1) {
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 24.dp))
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