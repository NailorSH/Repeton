package com.nailorsh.repeton.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nailorsh.repeton.R
import com.nailorsh.repeton.ui.components.SearchBarWithFilter
import com.nailorsh.repeton.ui.theme.RepetonTheme

@Composable
fun SearchScreen(modifier: Modifier = Modifier) {
    var query by remember { mutableStateOf("") }

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = modifier.padding(vertical = 15.dp),
    ) {
        Column (
            modifier = Modifier.fillMaxWidth(0.9f)
        ){
            SearchBarWithFilter(
                placeholder = R.string.search_placeholder,
                leadingIcon = R.drawable.search_icon,
                filterIcon = R.drawable.filter_icon,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search
                ),
                value = query,
                onValueChanged = { query = it},
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun TutorCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.padding(15.dp)
    ) {
        Column() {
            Row(
                horizontalArrangement = Arrangement.spacedBy(15.dp, Alignment.Start),
                verticalAlignment = Alignment.Top,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.man_photo),
                    contentDescription = "Репетитор",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .width(81.dp)
                        .height(81.dp)
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.Top),
                    horizontalAlignment = Alignment.Start
                ) {
                    Row(
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(18.dp)
                    ) {
                        Text(text = "hello")
                        Icon(
                            painter = painterResource(id = R.drawable.heart_icon),
                            contentDescription = ""
                        )
                    }
//                    Row() {
//                        Row() {
//                            Icon(painter =, contentDescription =)
//                            Text(text =)
//                        }
//                        Row() {
//                            Icon(painter =, contentDescription =)
//                            Text(text =)
//                        }
//                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun TutorCardPreview() {
    RepetonTheme {
        TutorCard()
    }
}

