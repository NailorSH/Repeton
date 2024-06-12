package com.nailorsh.repeton.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.nailorsh.repeton.R

@Composable
fun LikeButton(
    modifier: Modifier = Modifier,
    onButtonClicked: () -> Unit = {},
    isLiked: Boolean
) {
    val iconRes = if (isLiked) R.drawable.ic_liked_heart_icon else R.drawable.ic_unliked_heart_icon

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

