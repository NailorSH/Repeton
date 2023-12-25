package com.nailorsh.repeton.ui.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nailorsh.repeton.R
import com.nailorsh.repeton.ui.components.ExpandableText
import com.nailorsh.repeton.ui.components.IconWithText
import com.nailorsh.repeton.ui.components.LikeButton
import com.nailorsh.repeton.ui.components.RepetonButton
import com.nailorsh.repeton.ui.components.SearchBarWithFilter
import com.nailorsh.repeton.ui.theme.AmbientColor
import com.nailorsh.repeton.ui.theme.RepetonTheme
import com.nailorsh.repeton.ui.theme.SpotColor
import com.nailorsh.repeton.ui.theme.StarColor
import com.nailorsh.repeton.ui.theme.White
import com.nailorsh.repeton.ui.theme.WriteButtonBackgroundColor
import com.nailorsh.repeton.ui.theme.WriteButtonTextColor

@Composable
fun SearchScreen(modifier: Modifier = Modifier) {
    var query by remember { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = modifier.padding(top = 15.dp),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(18.dp, Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            SearchBarWithFilter(
                placeholder = R.string.search_placeholder,
                leadingIcon = R.drawable.search_icon,
                filterIcon = R.drawable.filter_icon,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search
                ),
                value = query,
                onValueChanged = { query = it },
                modifier = Modifier.fillMaxWidth()
            )

            TutorList()
        }
    }
}

@Composable
fun TutorList(modifier: Modifier = Modifier) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        item { TutorCard() }
        item { TutorCard() }
        item { TutorCard() }
    }
}

@Composable
fun TutorCard(
    modifier: Modifier = Modifier,
    onWriteButtonClicked: () -> Unit = {}
) {
    var isLiked by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(25.dp, Alignment.Top),
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 4.dp, spotColor = SpotColor, ambientColor = AmbientColor)
            .background(color = White, shape = RoundedCornerShape(size = 5.dp))
            .padding(15.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top),
            horizontalAlignment = Alignment.Start,
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(15.dp, Alignment.Start),
                verticalAlignment = Alignment.Top,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.man_photo),
                    contentDescription = "Репетитор",
                    contentScale = ContentScale.Crop,
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
                        horizontalArrangement = Arrangement.spacedBy(18.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Александр Витальевич",
                            style = TextStyle(
                                fontSize = 22.sp,
                                lineHeight = 26.sp,
                                fontWeight = FontWeight(800),
                                color = Color.Black,
                            ),
                            modifier = Modifier.weight(1f)
                        )

                        LikeButton(
                            modifier = Modifier
                                .padding(start = 9.dp, top = 5.dp)
                                .width(24.dp)
                                .height(22.dp),
                            isLiked = isLiked,
                            onButtonClicked = { isLiked = !isLiked }
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(9.dp, Alignment.Start),
                        verticalAlignment = Alignment.Top,
                    ) {
                        IconWithText(
                            icon = R.drawable.star_icon,
                            iconTint = StarColor,
                            text = "4,93"
                        )
                        IconWithText(
                            icon = R.drawable.comment_icon,
                            text = "100 отзывов"
                        )
                    }
                }
            }

            ExpandableText(
                modifier = modifier.fillMaxWidth(),
                text = "Я являюсь техническим руководителем проекта «Учу на Профи.Ру» и активно использую современные технологии распознавания "
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top),
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                InfoSection(
                    modifier = Modifier.fillMaxWidth(),
                    title = R.string.subjects_section,
                    body = "Математика • Информатика"
                )

                InfoSection(
                    modifier = Modifier.fillMaxWidth(),
                    title = R.string.education_section,
                    body = "Окончил МФТИ, ФОПФ, два красных диплома, 2005 г."
                )

                PriceSection(
                    modifier = Modifier.fillMaxWidth(),
                    title = R.string.prices_section,
                    subjectsPrices = mapOf(
                        "Математика" to "500-1000 ₽ / 60 мин",
                        "Информатика" to "800-1000 ₽ / 60 мин"
                    )
                )
            }
        }

        RepetonButton(
            modifier = Modifier
                .fillMaxWidth(),
            text = R.string.message_button,
            textColor = WriteButtonTextColor,
            buttonColor = WriteButtonBackgroundColor,
            onButtonClicked = onWriteButtonClicked
        )
    }
}



@Composable
fun InfoSection(
    modifier: Modifier = Modifier,
    @StringRes title: Int,
    body: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.Top),
        horizontalAlignment = Alignment.Start,
        modifier = modifier
    ) {
        Text(
            text = stringResource(title),
            style = TextStyle(
                fontSize = 18.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight(500),
                color = Color.Black,
            )
        )
        Text(
            text = body,
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight(400),
                color = Color.Black,
            )
        )
    }
}

@Composable
fun PriceSection(
    modifier: Modifier = Modifier,
    @StringRes title: Int,
    subjectsPrices: Map<String, String>
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.Top),
        horizontalAlignment = Alignment.Start,
        modifier = modifier
    ) {
        Text(
            text = stringResource(title),
            style = TextStyle(
                fontSize = 18.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight(500),
                color = Color.Black,
            ),
        )
        for (pair in subjectsPrices) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = pair.key,
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        fontWeight = FontWeight(400),
                        color = Color.Black,
                    ),
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = pair.value,
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        fontWeight = FontWeight(400),
                        color = Color.Black,
                    ),
                    textAlign = TextAlign.Right,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Preview(
    showSystemUi = true
)
@Composable
fun TutorCardPreview() {
    RepetonTheme {
        TutorCard()
    }
}

