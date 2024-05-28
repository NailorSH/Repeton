package com.nailorsh.repeton.features.tutorsearch.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nailorsh.repeton.R
import com.nailorsh.repeton.common.data.models.Id
import com.nailorsh.repeton.common.data.models.education.Education
import com.nailorsh.repeton.common.data.models.education.EducationType
import com.nailorsh.repeton.common.data.models.lesson.Subject
import com.nailorsh.repeton.common.data.models.lesson.SubjectWithPrice
import com.nailorsh.repeton.common.data.models.user.Tutor
import com.nailorsh.repeton.common.data.sources.FakeTutorsSource
import com.nailorsh.repeton.core.ui.components.IconWithText
import com.nailorsh.repeton.core.ui.components.UserImage
import com.nailorsh.repeton.core.ui.theme.RepetonTheme
import com.nailorsh.repeton.core.ui.theme.StarColor

@Composable
fun TutorCard(
    tutor: Tutor,
    modifier: Modifier = Modifier,
    onCardClicked: (Tutor) -> Unit
) {
    Card(
        modifier = modifier
            .clickable { onCardClicked(tutor) },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                UserImage(photoSrc = tutor.photoSrc, size = 81.dp)

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
                            text = "${tutor.name}\n${tutor.surname}",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(
                            dimensionResource(R.dimen.padding_medium),
                            Alignment.Start
                        ),
                        verticalAlignment = Alignment.Top,
                    ) {
                        IconWithText(
                            icon = R.drawable.ic_star_icon,
                            iconTint = StarColor,
                            text = tutor.rating.toString()
                        )
                        IconWithText(
                            icon = R.drawable.ic_comment_icon,
                            text = "${tutor.reviewsNumber} отзывов"
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top),
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                tutor.about?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

//                tutor.subjects?.let { subjects ->
//                    InfoSection(
//                        modifier = Modifier.fillMaxWidth(),
//                        title = R.string.subjects_section,
//                        body = subjects.joinToString(separator = " • ") { it.name }
//                    )
//                }

                tutor.educations?.let { educations ->
                    InfoSection(
                        modifier = Modifier.fillMaxWidth(),
                        title = R.string.education_section,
                        body = educations.joinToString(separator = "\n") { edu ->
                            " • ${edu.type.value}" + edu.specialization.let {
                                if (it != null) " - $it" else ""
                            }
                        }
                    )
                }

                tutor.subjectsPrices?.let {
                    PriceSection(
                        modifier = Modifier.fillMaxWidth(),
                        title = R.string.prices_section,
                        subjectsPrices = it
                    )
                }
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
        TutorCard(
            tutor = FakeTutorsSource.getTutorsList()[0].copy(
                educations = listOf(
                    Education(
                        id = Id(""),
                        type = EducationType.MASTER,
                    )
                ),
                subjectsPrices = listOf(
                    SubjectWithPrice(Subject(Id(""), "Математика"), 1000)
                )
            ),
            onCardClicked = {}
        )
    }
}