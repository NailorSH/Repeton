package com.nailorsh.repeton.features.tutorprofile.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nailorsh.repeton.R
import com.nailorsh.repeton.common.data.models.Id
import com.nailorsh.repeton.common.data.models.user.Tutor
import com.nailorsh.repeton.core.ui.components.ErrorScreen
import com.nailorsh.repeton.core.ui.components.LoadingScreen
import com.nailorsh.repeton.core.ui.components.TitleWithExpandableText
import com.nailorsh.repeton.core.ui.theme.RepetonTheme
import com.nailorsh.repeton.features.tutorprofile.presentation.ui.components.Contacts
import com.nailorsh.repeton.features.tutorprofile.presentation.ui.components.ExperienceYearsNumber
import com.nailorsh.repeton.features.tutorprofile.presentation.ui.components.LanguageSkills
import com.nailorsh.repeton.features.tutorprofile.presentation.ui.components.PriceInfo
import com.nailorsh.repeton.features.tutorprofile.presentation.ui.components.ProfileInfoRow
import com.nailorsh.repeton.features.tutorprofile.presentation.ui.components.Rating
import com.nailorsh.repeton.features.tutorprofile.presentation.ui.components.ReviewsNumber
import com.nailorsh.repeton.features.tutorprofile.presentation.ui.components.TaughtLessonsNumber
import com.nailorsh.repeton.features.tutorprofile.presentation.ui.components.TopTutorProfileBar
import com.nailorsh.repeton.features.tutorprofile.presentation.viewmodel.TutorProfileUiState
import com.nailorsh.repeton.features.tutorprofile.presentation.viewmodel.TutorProfileViewModel

@Composable
fun TutorProfileScreen(
    tutorId: Id,
    onBackClicked: () -> Unit,
    viewModel: TutorProfileViewModel = hiltViewModel(),
) {
    LaunchedEffect(tutorId) {
        viewModel.getTutorProfile(tutorId)
    }

    when (val tutorProfileState = viewModel.tutorProfileUiState) {
        is TutorProfileUiState.Loading -> LoadingScreen(modifier = Modifier.fillMaxSize())

        is TutorProfileUiState.Success -> TutorProfile(
            tutor = tutorProfileState.tutor,
            onBackClicked = onBackClicked
        )

        TutorProfileUiState.Error -> ErrorScreen(
            { viewModel.getTutorProfile(tutorId) },
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun TutorProfile(
    tutor: Tutor,
    onBackClicked: () -> Unit
) {
    Scaffold(
        topBar = {
            TopTutorProfileBar(
                onBackClick = onBackClicked,
//                onShareClick = {},
//                onFavoriteClick = {}
            )
        },
//        bottomBar = {
//            BottomTutorProfileBar(
//                onChatButtonClicked = {},
//                onTrialLessonButtonClicked = {}
//            )
//        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            TutorMainInfoBlock(tutor)

            HorizontalDivider(
                thickness = 3.dp,
                modifier = Modifier.padding(vertical = 20.dp)
            )

            TutorAdditionalInfoBlock(tutor)

//            HorizontalDivider(
//                thickness = 2.dp,
//                modifier = Modifier.padding(vertical = 20.dp)
//            )

//            WorkScheduleButton {/*TODO*/ }

//            HorizontalDivider(
//                modifier = Modifier.padding(vertical = dimensionResource(R.dimen.padding_medium))
//            )

//            AllSubjectsButton {/*TODO*/ }

            tutor.contacts?.let {
                HorizontalDivider(
                    thickness = 2.dp,
                    modifier = Modifier.padding(vertical = 20.dp)
                )

                Contacts(
                    contacts = it,
                    modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_medium))
                )
            }

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))
        }
    }
}

@Composable
private fun TutorMainInfoBlock(
    tutor: Tutor,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(
            horizontal = dimensionResource(R.dimen.padding_medium)
        )
    ) {
        ProfileInfoRow(
            profileImageSrc = tutor.photoSrc,
            name = "${tutor.name} ${tutor.surname.first()}.",
            country = tutor.location?.country
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

//        HorizontalDivider(
//            modifier = Modifier.padding(
//                vertical = dimensionResource(R.dimen.padding_medium)
//            )
//        )
//
//        Advantages()
    }
}

@Composable
private fun TutorAdditionalInfoBlock(
    tutor: Tutor,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(
            horizontal = dimensionResource(R.dimen.padding_medium)
        )
    ) {
        tutor.about?.let {
            TitleWithExpandableText(
                title = stringResource(R.string.about_me),
                text = it
            )
        }

        HorizontalDivider(
            modifier = Modifier.padding(
                vertical = dimensionResource(R.dimen.padding_medium)
            )
        )

        tutor.languagesWithLevels?.let { LanguageSkills(it) }
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun TutorProfileCardPreview() {
    RepetonTheme {
        TutorProfileScreen(Id("2"), {})
    }
}