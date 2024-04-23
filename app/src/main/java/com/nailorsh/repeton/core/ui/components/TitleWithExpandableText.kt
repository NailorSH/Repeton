package com.nailorsh.repeton.core.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import com.nailorsh.repeton.R

@Composable
fun TitleWithExpandableText(
    title: String,
    text: String,
    modifier: Modifier = Modifier,
    titleStyle: TextStyle = MaterialTheme.typography.titleLarge,
    expandableTextStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    expandableTextButtonStyle: TextStyle = MaterialTheme.typography.labelLarge
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            style = titleStyle,
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))

        ExpandableText(
            text = text,
            style = expandableTextStyle,
            textButtonStyle = expandableTextButtonStyle
        )
    }
}