package com.nailorsh.repeton.features.tutorprofile.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nailorsh.repeton.R
import com.nailorsh.repeton.common.data.models.Tutor
import com.nailorsh.repeton.common.data.models.getFlagEmoji
import com.nailorsh.repeton.common.data.sources.FakeTutorsSource
import com.nailorsh.repeton.core.ui.components.TitleWithExpandableText
import com.nailorsh.repeton.core.ui.theme.RepetonTheme
import com.nailorsh.repeton.features.tutorprofile.presentation.ui.components.Advantages
import com.nailorsh.repeton.features.tutorprofile.presentation.ui.components.BottomTutorProfileBar
import com.nailorsh.repeton.features.tutorprofile.presentation.ui.components.ExperienceYearsNumber
import com.nailorsh.repeton.features.tutorprofile.presentation.ui.components.LanguageSkills
import com.nailorsh.repeton.features.tutorprofile.presentation.ui.components.PriceInfo
import com.nailorsh.repeton.features.tutorprofile.presentation.ui.components.ProfileInfoRow
import com.nailorsh.repeton.features.tutorprofile.presentation.ui.components.Rating
import com.nailorsh.repeton.features.tutorprofile.presentation.ui.components.ReviewsNumber
import com.nailorsh.repeton.features.tutorprofile.presentation.ui.components.TaughtLessonsNumber
import com.nailorsh.repeton.features.tutorprofile.presentation.ui.components.TopTutorProfileBar

@Composable
fun TutorProfileScreen(tutor: Tutor) {
    Scaffold(
        topBar = {
            TopTutorProfileBar(
                onBackClick = {},
                onShareClick = {},
                onFavoriteClick = {}
            )
        },
        bottomBar = {
            BottomTutorProfileBar(
                onChatButtonClicked = {},
                onTrialLessonButtonClicked = {}
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier.padding(
                    horizontal = dimensionResource(R.dimen.padding_medium)
                )
            ) {
                ProfileInfoRow(
                    profileImageId = R.drawable.man_photo,
                    name = "${tutor.name} ${tutor.surname.first()}.",
                    country = tutor.country,
                    flagEmoji = tutor.getFlagEmoji()
                )

                HorizontalDivider(
                    modifier = Modifier.padding(
                        vertical = dimensionResource(R.dimen.padding_medium)
                    )
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Rating(tutor.rating.toString())
                    ReviewsNumber(tutor.reviewsNumber)
                    PriceInfo(tutor.averagePrice)
                    TaughtLessonsNumber(tutor.taughtLessonNumber)
                    ExperienceYearsNumber(tutor.experienceYears)
                }

                HorizontalDivider(
                    modifier = Modifier.padding(
                        vertical = dimensionResource(R.dimen.padding_medium)
                    )
                )

                Advantages()
            }

            HorizontalDivider(
                thickness = 3.dp,
                modifier = Modifier.padding(vertical = 20.dp)
            )

            Column(
                modifier = Modifier.padding(
                    horizontal = dimensionResource(R.dimen.padding_medium)
                )
            ) {
                TitleWithExpandableText(
                    title = stringResource(R.string.about_me),
                    text = tutor.about
                )

                HorizontalDivider(
                    modifier = Modifier.padding(
                        vertical = dimensionResource(R.dimen.padding_medium)
                    )
                )

                LanguageSkills(tutor.languages)
            }

            HorizontalDivider(
                thickness = 2.dp,
                modifier = Modifier.padding(vertical = 20.dp)
            )

            OutlinedButton(
                onClick = { /*TODO*/ },
                shape = MaterialTheme.shapes.extraLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(R.dimen.padding_medium))
            ) {
                Icon(
                    painter = painterResource(R.drawable.outline_date_range_24),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(dimensionResource(R.dimen.padding_small)))
                Text(
                    text = stringResource(R.string.my_work_schedule),
                    style = MaterialTheme.typography.titleMedium
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = dimensionResource(R.dimen.padding_medium))
            )

            TextButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(R.dimen.padding_medium))
            ) {
                Text(
                    text = stringResource(R.string.other_subjects),
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun TutorProfileCardPreview() {
    RepetonTheme {
        TutorProfileScreen(FakeTutorsSource.getTutorsList()[1])
    }
}