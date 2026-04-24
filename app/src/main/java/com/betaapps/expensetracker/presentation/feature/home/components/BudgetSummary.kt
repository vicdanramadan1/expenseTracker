package com.betaapps.expensetracker.presentation.feature.home.components

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.betaapps.expensetracker.R
import com.betaapps.expensetracker.ui.theme.BudgetSafe
import com.betaapps.expensetracker.ui.theme.ExpenseTrackerTheme
import com.betaapps.expensetracker.ui.theme.TextSecondary

@SuppressLint("SuspiciousIndentation")
@Composable
fun BudgetSummary(
    modifier: Modifier = Modifier,
    spentAmount: Double = 0.0,
    budgetAmount: Double = 0.0,
    statusColor: Color = BudgetSafe,
    topSpending: String = "",
    statusChipText: Int,
    onEditBudgetClick: () -> Unit,
    onEditMothClick: () -> Unit
) {
    val progress = (spentAmount / budgetAmount)
        .toFloat()
        .coerceIn(0f, 1f)

    Card(
        modifier = modifier
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(18.dp),
                ambientColor = MaterialTheme.colorScheme.scrim,
                spotColor = MaterialTheme.colorScheme.scrim
            ),
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.budget_summary_title),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                TextButton(
                    onClick = onEditMothClick,
                ) {
                    Icon(
                        Icons.Default.EditCalendar,
                        contentDescription = null,
                        tint = TextSecondary,
                    )
                    Text(
                        text = "April 2026",
                        modifier = Modifier.padding(horizontal = 4.dp),
                        color = TextSecondary
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BudgetRing(progress = progress)

                Spacer(modifier = Modifier.width(18.dp))

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = "${spentAmount}/${budgetAmount}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Row(verticalAlignment = Alignment.CenterVertically)
                    {
                        Text(
                            text = stringResource(
                                R.string.budget_remaining,
                            ),
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                        Text(
                            text = (budgetAmount - spentAmount).toString() + "TRY",
                            color = statusColor,
                            style = MaterialTheme.typography.bodyMedium,
                        )

                    }

                    StatusChip(
                        text = statusChipText,
                        color = statusColor
                    )
                }
            }
        }

        HorizontalDivider(
            color = TextSecondary.copy(alpha = .5f),
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            )
            {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(color = statusColor.copy(alpha = .2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.BarChart,
                        contentDescription = "Chart Icon",
                        tint = statusColor,
                    )
                }
                Text(
                    text = stringResource(
                        R.string.budget_top_spending,
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis

                )
                Text(
                    text = topSpending,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            TextButton(
                onClick = onEditBudgetClick,
            ) {
                Text("Edit budget")
            }
        }
    }
}

@Composable
private fun BudgetRing(
    progress: Float,
    color: Color = BudgetSafe
) {
    val textColor = MaterialTheme.colorScheme.onSurface
    val ringColor = remember { color.copy(alpha = .1f) }

    Box(
        modifier = Modifier.size(96.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(96.dp)) {
            val strokeWidth = 12.dp.toPx()
            drawCircle(
                color = ringColor,
                radius = (size.minDimension - strokeWidth) / 2f,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = progress * 360f,
                useCenter = false,
                topLeft = Offset(strokeWidth / 2f, strokeWidth / 2f),
                size = androidx.compose.ui.geometry.Size(
                    size.width - strokeWidth,
                    size.height - strokeWidth
                ),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }

        Text(
            text = "30%",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BudgetSummaryPreview() {
    ExpenseTrackerTheme {
        BudgetSummary(
            modifier = Modifier.padding(16.dp),
            spentAmount = 180.0,
            budgetAmount = 200.0,
            statusChipText = R.string.budget_on_track,
            onEditBudgetClick = {},
            onEditMothClick = {}
        )
    }
}
