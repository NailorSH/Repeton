package com.nailorsh.repeton.features.userprofile.presentation.ui.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nailorsh.repeton.R
import com.nailorsh.repeton.features.userprofile.data.TrailingContent

@Composable
fun ProfileMenuItem(
    @DrawableRes icon: Int,
    @StringRes text: Int,
    onPressCallback: () -> Unit,
    trailingItem: TrailingContent,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable { onPressCallback() }
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier
                .weight(0.1f)

        )
        Text(
            text = stringResource(text),
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        )
        Box(
            contentAlignment = Alignment.TopEnd,
            modifier = Modifier
                .weight(0.35f)
                .padding(end = 16.dp)

        ) {
            trailingItem.Content(modifier = Modifier)
        }

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileMenuItemPreview() {
    ProfileMenuItem(
        icon = R.drawable.ic_filter_icon,
        text = R.string.search_placeholder,
        onPressCallback = {},
        trailingItem = TrailingContent.Empty
    )
}