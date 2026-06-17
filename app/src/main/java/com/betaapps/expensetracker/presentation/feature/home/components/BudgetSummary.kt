package com.betaapps.expensetracker.presentation.feature.home.components

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.betaapps.expensetracker.R
import com.betaapps.expensetracker.ui.theme.BudgetSafe
import com.betaapps.expensetracker.ui.theme.BorderSubtle
import com.betaapps.expensetracker.ui.theme.ExpenseTrackerTheme
import com.betaapps.expensetracker.ui.theme.TextSecondary
import kotlin.math.roundToInt

@Composable
fun BudgetSummary(
    modifier: Modifier = Modifier,
    spentAmount: Double = 0.0,
    budgetAmount: Double = 0.0,
    statusColor: Color = BudgetSafe,
    selectedMonthText: String,
    @StringRes statusChipText: Int,
    onMonthClick: () -> Unit
) {
    val progress = if (budgetAmount > 0.0) {
        (spentAmount / budgetAmount).toFloat().coerceIn(0f, 1f)
    } else {
        0f
    }
    val remainingAmount = (budgetAmount - spentAmount).coerceAtLeast(0.0)
    val plannedLaterAmount = (budgetAmount * 0.005).coerceAtMost(remainingAmount)

    Card(
        modifier = modifier
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(22.dp),
                ambientColor = Color(0x1A111827),
                spotColor = Color(0x22111827)
            ),
        shape = RoundedCornerShape(22.dp),
        border = BorderStroke(1.dp, BorderSubtle.copy(alpha = 0.72f)),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.budget_summary_title),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    style = MaterialTheme.typography.headlineSmall,
                    fontSize = 20.sp,
                    lineHeight = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                MonthPill(
                    selectedMonthText = selectedMonthText,
                    onClick = onMonthClick
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BudgetWalletMark(color = statusColor)

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = stringResource(R.string.currency_amount, spentAmount),
                            modifier = Modifier.weight(1f, fill = false),
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.budget_spent_label),
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.titleMedium,
                            fontSize = 14.sp,
                            lineHeight = 18.sp,
                            maxLines = 1
                        )
                    }

                    Text(
                        text = stringResource(
                            R.string.budget_of_amount_budget,
                            stringResource(R.string.currency_amount, budgetAmount)
                        ),
                        color = TextSecondary,
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 14.sp,
                        lineHeight = 18.sp
                    )

                    BudgetProgress(
                        progress = progress,
                        progressColor = statusColor
                    )

                    BudgetStatusChip(
                        text = statusChipText,
                        color = statusColor,
                        modifier = Modifier.align(Alignment.Start)
                    )
                }
            }

            HorizontalDivider(color = BorderSubtle)

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BudgetMetric(
                    icon = Icons.Default.AttachMoney,
                    label = stringResource(R.string.budget_remaining_clean),
                    value = stringResource(R.string.currency_amount, remainingAmount),
                    valueColor = statusColor,
                    modifier = Modifier.weight(1f)
                )

                Box(
                    modifier = Modifier
                        .height(58.dp)
                        .width(1.dp)
                        .background(BorderSubtle)
                )

                BudgetMetric(
                    icon = Icons.Default.Schedule,
                    label = stringResource(R.string.budget_planned_later),
                    value = stringResource(R.string.currency_amount, plannedLaterAmount),
                    valueColor = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 14.dp)
                )
            }
        }
    }
}

@Composable
private fun MonthPill(
    selectedMonthText: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, BorderSubtle, RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .defaultMinSize(minHeight = 38.dp)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = Icons.Default.DateRange,
            contentDescription = stringResource(R.string.budget_select_month_content_description),
            tint = TextSecondary,
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = selectedMonthText,
            color = TextSecondary,
            style = MaterialTheme.typography.titleMedium,
            fontSize = 14.sp,
            lineHeight = 18.sp,
            maxLines = 1
        )
        Icon(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = null,
            tint = TextSecondary,
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
private fun BudgetWalletMark(
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(80.dp)
            .background(color.copy(alpha = 0.10f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.AccountBalanceWallet,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(48.dp)
        )
    }
}

@Composable
private fun BudgetProgress(
    progress: Float,
    progressColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(6.dp)
                .clip(RoundedCornerShape(99.dp))
                .background(BorderSubtle.copy(alpha = 0.78f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .height(6.dp)
                    .clip(RoundedCornerShape(99.dp))
                    .background(progressColor)
            )
        }

        Text(
            text = "${(progress * 100f).roundToInt()}%",
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleMedium,
            fontSize = 14.sp,
            lineHeight = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.End,
            modifier = Modifier.width(42.dp)
        )
    }
}

@Composable
private fun BudgetStatusChip(
    @StringRes text: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(color.copy(alpha = 0.10f))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = stringResource(text),
            color = color,
            style = MaterialTheme.typography.titleMedium,
            fontSize = 14.sp,
            lineHeight = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun BudgetMetric(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    valueColor: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .background(BudgetSafe.copy(alpha = 0.10f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = BudgetSafe,
                modifier = Modifier.size(24.dp)
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = label,
                color = TextSecondary,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 13.sp,
                lineHeight = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = value,
                color = valueColor,
                style = MaterialTheme.typography.titleLarge,
                fontSize = 18.sp,
                lineHeight = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BudgetSummaryPreview() {
    ExpenseTrackerTheme {
        BudgetSummary(
            spentAmount = 20.0,
            budgetAmount = 2000.0,
            selectedMonthText = "Jun 2026",
            statusChipText = R.string.budget_on_track_short,
            onMonthClick = {}
        )
    }
}
