package com.nailorsh.repeton.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nailorsh.repeton.R

@Composable
fun RepetonButton(
    modifier: Modifier = Modifier,
    @StringRes text: Int,
    textStyle: TextStyle = TextStyle(
        fontSize = 14.sp,
        lineHeight = 32.sp,
        fontWeight = FontWeight(600),
        color = Color.Black
    ),
    buttonColor: Color = Color.LightGray,
    onButtonClicked: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clickable { onButtonClicked() }
            .background(
                color = buttonColor,
                shape = RoundedCornerShape(size = 8.dp)
            )
            .padding(horizontal = 5.dp, vertical = 10.dp)
    ) {
        Text(
            text = stringResource(text),
            style = textStyle
        )
    }
}

@Composable
fun LikeButton(
    modifier: Modifier = Modifier,
    onButtonClicked: () -> Unit = {},
    isLiked: Boolean
) {
    val iconRes = if (isLiked) R.drawable.liked_heart_icon else R.drawable.unliked_heart_icon

    Icon(
        imageVector = ImageVector.vectorResource(iconRes),
        contentDescription = "",
        tint = ImageVector.vectorResource(iconRes).tintColor,
        modifier = modifier
            .clickable {
                onButtonClicked()
            }
    )
}