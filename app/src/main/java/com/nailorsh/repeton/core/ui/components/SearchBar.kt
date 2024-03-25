package com.nailorsh.repeton.core.ui.components

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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nailorsh.repeton.core.ui.theme.Black
import com.nailorsh.repeton.core.ui.theme.SearchPlaceholderColor
import com.nailorsh.repeton.core.ui.theme.TextFieldTextColor
import com.nailorsh.repeton.core.ui.theme.White
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun triggerSearch(
    getSearchResults: () -> Unit,
    newQuery: String
): Job? {
    var searchJob by remember { mutableStateOf<Job?>(null) }

    DisposableEffect(Unit) {
        val coroutineScope = CoroutineScope(Dispatchers.IO)
        searchJob?.cancel()
        searchJob = coroutineScope.launch {
            delay(600) // Задержка 600 мс
            getSearchResults() // Вызов функции поиска после задержки
        }
        onDispose {
            searchJob?.cancel() // Отмена предыдущего запроса при удалении компонента
        }
    }
    return searchJob
}

@Composable
fun SearchBarWithFilter(
    @StringRes placeholder: Int,
    @DrawableRes leadingIcon: Int,
    @DrawableRes filterIcon: Int,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    value: String,
    onValueChanged: (String) -> Unit,
    onSearch: () -> Unit,
    onFilterClicked: () -> Unit = { },
    modifier: Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .border(
                width = 2.dp,
                color = Black,
                shape = RoundedCornerShape(size = 5.dp)
            )
            .fillMaxWidth()
            .background(
                color = White,
                shape = RoundedCornerShape(size = 5.dp)
            )
            .padding(10.dp)
    ) {
        SearchBar(
            modifier = Modifier.weight(1f),
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            value = value,
            onValueChanged = onValueChanged,
            onSearch = onSearch
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
    keyboardActions: KeyboardActions,
    value: String,
    onSearch: () -> Unit,
    onValueChanged: (String) -> Unit,
) {
    var searchJob by remember { mutableStateOf<Job?>(null) }

    BasicTextField(
        value = value,
        onValueChange = {
            onValueChanged(it)
        },
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = true,
        textStyle = TextStyle(
            fontSize = 14.sp,
            lineHeight = 17.sp,
            fontWeight = FontWeight(400),
            color = TextFieldTextColor
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
                            color = SearchPlaceholderColor,
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                innerTextField()
            }
        }
    )
}

//@Preview(
//    showSystemUi = true
//)
//@Composable
//fun SearchBarPreview() {
//    RepetonTheme {
//        SearchBarWithFilter(
//            placeholder = R.string.search_placeholder,
//            leadingIcon = R.drawable.search_icon,
//            filterIcon = R.drawable.filter_icon,
//            keyboardOptions = KeyboardOptions(
//                keyboardType = KeyboardType.Text,
//                imeAction = ImeAction.Search
//            ),
//            keyboardActions = KeyboardActions(
//                onSearch = {  }
//            ),
//            onSearch = {},
////            onValueChanged = {},
//            modifier = Modifier,
////            value = ""
//        )
//    }
//}