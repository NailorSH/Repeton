package com.nailorsh.repeton.features.userprofile.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nailorsh.repeton.features.userprofile.data.Options

@Composable
fun ProfileOptions(
    optionsList: List<Options>,
    onOptionClicked : (Options) -> Unit,
    isSettings : Boolean,
    modifier : Modifier = Modifier
) {
    ElevatedCard(
        shape = if (isSettings) RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomEnd = 0.dp, bottomStart = 0.dp)
                else CardDefaults.elevatedShape,
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
                    trailingItem = item.trailingItem
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
    ProfileOptions(listOf(), {}, true)
}