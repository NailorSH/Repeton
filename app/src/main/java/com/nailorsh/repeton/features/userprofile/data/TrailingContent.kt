package com.nailorsh.repeton.features.userprofile.data

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Badge
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


interface TrailingContent {

    @Composable
    fun Content(modifier: Modifier)

    companion object {
        val Empty = object : TrailingContent {
            @Composable
            override fun Content(modifier: Modifier) = Unit
        }
    }
}

class HomeworkBadge(count: Int) : TrailingContent {
    private var _count by mutableStateOf(count)

    @Composable
    override fun Content(modifier: Modifier) {
        Badge(
            modifier = modifier
                .clickable { _count++ }
        ) {
            Text(text = _count.toString())
        }
    }


}

class ThemeSwitcher(switchState: Boolean, onSwitchCallback: (Boolean) -> Unit) : TrailingContent {

    private var state by mutableStateOf(switchState)
    private var onSwitchCallback by mutableStateOf(onSwitchCallback)

    @Composable
    override fun Content(modifier: Modifier) {
        Switch(
            checked = state,
            onCheckedChange = {
                onSwitchCallback(it)
            },
            modifier = modifier
                .heightIn(max = 24.dp)
                .widthIn(max = 64.dp)
        )
    }


}