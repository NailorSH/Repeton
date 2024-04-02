package com.nailorsh.repeton.currentlesson.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.AbsoluteCutCornerShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nailorsh.repeton.R

@Composable
fun AdditionalMaterialsCard(additionalMaterials: String?, modifier: Modifier = Modifier) {
    Box(modifier) {
        Column(modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))) {
            Text(
                text = stringResource(R.string.additional_materials),
                style = MaterialTheme.typography.titleMedium

            )
            Spacer(modifier = Modifier.height(12.dp))
            Card(
                shape = AbsoluteCutCornerShape(0.dp),
                modifier = Modifier
                    .shadow(elevation = 1.dp)
                    .fillMaxWidth()
                    .heightIn(min = 50.dp)
            ) {
                Text(
                    text = additionalMaterials ?: stringResource(R.string.no_additional_materials),

                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(8.dp)
                )
            }

        }
    }
}
