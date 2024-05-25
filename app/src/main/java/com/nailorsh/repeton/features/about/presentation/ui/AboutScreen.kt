package com.nailorsh.repeton.features.about.presentation.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nailorsh.repeton.common.data.models.Id
import com.nailorsh.repeton.common.data.models.education.Education
import com.nailorsh.repeton.features.about.presentation.ui.components.AboutBioCard
import com.nailorsh.repeton.features.about.presentation.ui.components.AboutEducation
import com.nailorsh.repeton.features.about.presentation.ui.components.AboutHeader
import com.nailorsh.repeton.features.about.presentation.ui.components.AboutTopBar
import com.nailorsh.repeton.features.about.presentation.viewmodel.AboutAction
import com.nailorsh.repeton.features.about.presentation.viewmodel.AboutState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    aboutState: AboutState,
    onAction: (AboutAction) -> Unit
) {
    val scaffoldState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {}, topBar = {
            AboutTopBar(
                isExpanded = aboutState.changeOptionsIsExpanded,
                showChangeOptions = { onAction(AboutAction.UpdateShowChangeOptions(it)) },
                onChangeAbout = { onAction(AboutAction.UpdateShowChangeAbout(true)) },
                onChangePhoto = { onAction(AboutAction.UpdateShowChangePhoto) },
                onNavigateBack = { onAction(AboutAction.NavigateBack) }
            )
        }) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                AboutHeader(
                    name = aboutState.name,
                    surname = aboutState.surname,
                    photoSrc = aboutState.photoSrc,
                    isTutor = aboutState.isTutor
                )
            }
            item { AboutBioCard(biography = aboutState.about) }
            item {
                HorizontalDivider(
                    modifier = Modifier.padding(
                        top = 24.dp, bottom = 18.dp, start = 48.dp, end = 48.dp
                    )
                )
            }
            item {
                AboutEducation(
                    education = aboutState.education?.name,
                    specialization = aboutState.education?.specialization,
                    educationsList = aboutState.educationList,
                    onEducationChange = { onAction(AboutAction.ChangeEducation(it)) },
                    onSpecializationChange = { onAction(AboutAction.ChangeSpecialization(it)) },
                    isExpanded = aboutState.educationListIsExpanded,
                    onExpandedChange = { onAction(AboutAction.UpdateShowEducationList(it)) },
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }
            item {
                HorizontalDivider(
                    modifier = Modifier.padding(
                        top = 24.dp, bottom = 18.dp, start = 48.dp, end = 48.dp
                    )
                )
            }
        }
    }
}


@Preview
@Composable
fun PreviewAboutScreen() {
    AboutScreen(aboutState = AboutState(
        education = Education(id = Id("-1"), name = "Bach"),
        about = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. " +
                "Lorem Ipsum has been the industry's standard dummy text ever since" +
                " the 1500s, when an unknown printer took a galley of type and scrambled it " +
                "to make a type specimen book. It has survived not only five centuries, but" +
                " also the leap into electronic typesetting, remaining essentially unchanged."
    ), onAction = {})
}