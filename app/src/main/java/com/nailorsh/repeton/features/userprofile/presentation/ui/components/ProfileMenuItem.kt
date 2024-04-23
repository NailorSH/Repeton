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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nailorsh.repeton.R

@Composable
fun ProfileMenuItem(
    @DrawableRes icon: Int,
    @StringRes text: Int,
    onPressCallback: () -> Unit,
    trailingItem: (@Composable () -> Unit)?,
) {
    Row(
        modifier = Modifier
            .clickable { onPressCallback() }
            .padding(vertical = 12.dp)
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier
                .weight(0.1f)
        )
        Text(
            text = stringResource(text),
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier
                .weight(0.4f)
        )
        Box(
            modifier = Modifier
                .weight(0.5f)
        ) {
            if (trailingItem != null) {
                trailingItem()
            }
        }

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileMenuItemPreview() {
    ProfileMenuItem(
        icon = R.drawable.filter_icon,
        text = R.string.search_placeholder,
        onPressCallback = {},
        trailingItem = {}
    )
}