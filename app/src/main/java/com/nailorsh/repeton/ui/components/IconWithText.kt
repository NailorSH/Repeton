package com.nailorsh.repeton.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun IconWithText(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    text: String,
    iconTint: Color = Color.Black
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(7.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(icon),
            tint = iconTint,
            contentDescription = null,
            modifier = Modifier
                .padding(1.dp)
                .height(15.dp)
        )
        Text(
            text = text,
            style = TextStyle(
                fontSize = 14.sp,
                lineHeight = 26.sp,
                fontWeight = FontWeight(500),
                color = Color.Black,
            )
        )
    }
}