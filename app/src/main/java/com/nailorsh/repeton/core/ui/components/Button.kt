package com.nailorsh.repeton.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nailorsh.repeton.R
import com.nailorsh.repeton.core.ui.theme.WriteButtonBackgroundColor
import com.nailorsh.repeton.core.ui.theme.WriteButtonTextColor

@Composable
fun RepetonButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(size = 8.dp),
    contentPadding: PaddingValues = PaddingValues(horizontal = 5.dp, vertical = 10.dp),
    buttonColor: Color = Color.LightGray,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
        content = content,
        modifier = modifier
            .clickable(
                onClick = onClick,
                enabled = enabled,
                role = Role.Button,
            )
            .background(
                color = buttonColor,
                shape = shape
            )
            .padding(contentPadding)
    )
}

@Preview(showSystemUi = true)
@Composable
fun RepetonButtonPreview() {
    RepetonButton(
        modifier = Modifier
            .fillMaxWidth(),
        buttonColor = WriteButtonBackgroundColor,
        onClick = {  }
    ) {
        Text(
            text = stringResource(R.string.message_button),
            style = TextStyle(
                fontSize = 14.sp,
                lineHeight = 32.sp,
                fontWeight = FontWeight(600),
                color = WriteButtonTextColor
            ),
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

