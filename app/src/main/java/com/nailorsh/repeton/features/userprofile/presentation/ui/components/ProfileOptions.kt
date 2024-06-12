package com.nailorsh.repeton.features.userprofile.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nailorsh.repeton.features.userprofile.data.HomeworkBadge
import com.nailorsh.repeton.features.userprofile.data.Options
import com.nailorsh.repeton.features.userprofile.data.ThemeSwitcher
import com.nailorsh.repeton.features.userprofile.data.TrailingContent
import com.nailorsh.repeton.features.userprofile.data.TrailingContentType

@Composable
fun ProfileOptions(
    optionsList: List<Options>,
    onOptionClicked : (Options) -> Unit,
    modifier : Modifier = Modifier
) {
    ElevatedCard(
        shape = RectangleShape,
        colors = CardDefaults.elevatedCardColors(),
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
                    isLogout = (item is Options.Logout),
                    icon = item.icon,
                    text = item.text,
                    onPressCallback = { onOptionClicked(item) },
                    trailingItem = when(val currentItem = item.trailingItem) {
                        TrailingContentType.Empty -> TrailingContent.Empty
                        is TrailingContentType.HomeworkBadge -> HomeworkBadge.invoke(currentItem.count)
                        is TrailingContentType.ThemeSwitcher -> ThemeSwitcher.invoke(currentItem.isEnabled, currentItem.onSwitchCallback)
                    }
                )
                if (index != optionsList.lastIndex) {
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