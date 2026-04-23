package com.betaapps.expensetracker.presentation.feature.home.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.betaapps.expensetracker.R
import com.betaapps.expensetracker.ui.theme.BudgetNegative

@Composable
fun StatusChip(
    modifier: Modifier = Modifier,
    @StringRes text: Int,
    color: Color
) {

        Row(
            modifier = modifier
                .background(color= color.copy(alpha = .2f), shape = RoundedCornerShape(16.dp))
                .padding(vertical = 4.dp, horizontal = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier =
                    Modifier
                        .size(5.dp)
                        .background(color = color , shape = CircleShape)

            )
            Text(
                text = stringResource(text),
                color = color,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(start = 2.dp)
            )
        }
    }

@Preview
@Composable
private fun StatusChipPreview() {
    StatusChip(
        color = BudgetNegative,
        text = R.string.budget_summary_title
    )

}