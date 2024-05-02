package com.nailorsh.repeton.features.userprofile.data

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Badge
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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

@Stable
class HomeworkBadge private constructor(count: Int) : TrailingContent {
    private var count by mutableStateOf(count)

    @Composable
    override fun Content(modifier: Modifier) {
        Badge(
            modifier = modifier
                .clickable { count++ }
        ) {
            Text(text = count.toString())
        }
    }

    companion object {
        @Composable
        fun invoke(
            count : Int
        ) : HomeworkBadge = remember { HomeworkBadge(count) }.apply {
            this.count = count
        }
    }

}

@Stable
class ThemeSwitcher private constructor(switchState: Boolean, onSwitchCallback: (Boolean) -> Unit) : TrailingContent {

    private var switchState by mutableStateOf(switchState)
    private var onSwitchCallback by mutableStateOf(onSwitchCallback)

    @Composable
    override fun Content(modifier: Modifier) {
        Switch(
            checked = switchState,
            onCheckedChange = {
                onSwitchCallback(it)
            },
            modifier = modifier
                .heightIn(max = 24.dp)
                .widthIn(max = 64.dp)
        )
    }

    companion object {
        @Composable
        fun invoke(
            switchState: Boolean,
            onSwitchCallback: (Boolean) -> Unit
        ) : ThemeSwitcher = remember { ThemeSwitcher(switchState, onSwitchCallback) }.apply {
            this.switchState = switchState
            this.onSwitchCallback = onSwitchCallback
        }
    }
}