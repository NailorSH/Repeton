package com.nailorsh.repeton.features.tutorsearch.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nailorsh.repeton.R
import com.nailorsh.repeton.core.ui.theme.RepetonTheme

@Composable
fun QueryHistoryItem(
    query: String,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier
        .clickable { onClick(query) }
        .padding(14.dp)
        .fillMaxWidth()) {
        Icon(
            modifier = Modifier.padding(end = 10.dp),
            imageVector = ImageVector.vectorResource(R.drawable.ic_history),
            contentDescription = null
        )
        Text(text = query)
    }
}

@Preview
@Composable
fun QueryHistoryItemPreview() {
    RepetonTheme {
        QueryHistoryItem("недавнее", {})
    }
}