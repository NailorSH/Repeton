package com.nailorsh.repeton.core.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.nailorsh.repeton.R

@Composable
fun ExpandableText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyLarge,
    textButtonStyle: TextStyle = MaterialTheme.typography.labelLarge,
) {
    Column(
        modifier = modifier
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
    ) {
        var seeMore by remember { mutableStateOf(true) }

        Text(
            text = text,
            style = style,
            maxLines = if (seeMore) 3 else Int.MAX_VALUE,
            overflow = TextOverflow.Ellipsis,
        )

        val textButton= if (seeMore) {
            stringResource(id = R.string.see_more)
        } else {
            stringResource(id = R.string.see_less)
        }

        Text(
            text = textButton,
            style = textButtonStyle,
            modifier = Modifier
                .clickable {
                    seeMore = !seeMore
                }
        )
    }
}


@Preview(
    showSystemUi = true
)
@Composable
fun ExpandableTextPreview() {
    ExpandableText(text = "Для тех кто работал с Compose, нет никаких проблем реализовать данный экран при помощи стандартных компонентов. Однако если обратить внимание на поле Bio - можно заметить, что оно оканчивается строкой ... more. При нажатии на которое разворачивается полный текст. ")
}
