package com.nailorsh.repeton.ui.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nailorsh.repeton.R
import com.nailorsh.repeton.ui.theme.RepetonTheme

@Composable
fun SearchBarWithFilter(
    @StringRes placeholder: Int,
    @DrawableRes leadingIcon: Int,
    @DrawableRes filterIcon: Int,
    keyboardOptions: KeyboardOptions,
    value: String,
    onValueChanged: (String) -> Unit,
    onFilterClicked: () -> Unit = { },
    modifier: Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .border(
                width = 2.dp,
                color = Color.Black,
                shape = RoundedCornerShape(size = 5.dp)
            )
            .fillMaxWidth()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(size = 5.dp)
            )
            .padding(10.dp)
    ) {
        SearchBar(
            modifier = Modifier.weight(1f),
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            keyboardOptions = keyboardOptions,
            value = value,
            onValueChanged = onValueChanged
        )

        Icon(
            imageVector = ImageVector.vectorResource(filterIcon),
            contentDescription = null,
            modifier = Modifier
                .width(21.dp)
                .height(17.dp)
                .clickable { onFilterClicked() }
        )
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    @StringRes placeholder: Int,
    @DrawableRes leadingIcon: Int,
    keyboardOptions: KeyboardOptions,
    value: String,
    onValueChanged: (String) -> Unit,
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChanged,
        keyboardOptions = keyboardOptions,
        singleLine = true,
        textStyle = TextStyle(
            fontSize = 14.sp,
            lineHeight = 17.sp,
            fontWeight = FontWeight(400),
            color = Color.Black
        ),
        modifier = modifier,
        decorationBox = { innerTextField ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(leadingIcon),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(1.dp)
                        .width(14.dp)
                        .height(14.dp)
                )

                if (value.isEmpty()) {
                    Text(
                        text = stringResource(placeholder),
                        style = TextStyle(
                            fontSize = 14.sp,
                            lineHeight = 17.sp,
                            fontWeight = FontWeight(400),
                            color = Color(R.color.search_placeholder),
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                innerTextField()
            }
        }
    )
}

@Preview(
    showSystemUi = true
)
@Composable
fun SearchBarPreview() {
    RepetonTheme {
        SearchBarWithFilter(
            placeholder = R.string.search_placeholder,
            leadingIcon = R.drawable.search_icon,
            filterIcon = R.drawable.filter_icon,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            onValueChanged = {},
            modifier = Modifier,
            value = ""
        )
    }
}