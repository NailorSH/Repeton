package com.nailorsh.repeton.currentlesson.presentation.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nailorsh.repeton.R
import com.nailorsh.repeton.core.ui.components.RepetonButton
import com.nailorsh.repeton.core.ui.theme.HomeWorkButtonColor


@Composable
fun HomeworkCard(hwLink: String?, modifier: Modifier = Modifier) {
    Box(modifier) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = stringResource(R.string.homework),
                style = MaterialTheme.typography.headlineSmall,

            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                colors = ButtonDefaults.buttonColors(),
                shape = MaterialTheme.shapes.small,
                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp),
                onClick = {
                    /* TODO Open Link */
                }
            ) {
                Text(
                    text = stringResource(R.string.to_homework_button),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                )

            }
        }
    }
}
