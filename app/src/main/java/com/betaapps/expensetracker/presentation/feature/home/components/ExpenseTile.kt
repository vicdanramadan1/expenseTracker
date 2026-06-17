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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.betaapps.expensetracker.R
import com.betaapps.expensetracker.presentation.feature.home.model.Expense
import com.betaapps.expensetracker.presentation.feature.home.model.ExpenseCategory
import com.betaapps.expensetracker.ui.theme.BudgetSafe

@Composable
fun ExpenseTile(
    expense: Expense,
    modifier: Modifier = Modifier,
    showContainer: Boolean = true,
    onClick: (Expense) -> Unit = {}
) {
    val shape = RoundedCornerShape(18.dp)

    if (showContainer) {
        Card(
            onClick = { onClick(expense) },
            modifier = modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 6.dp,
                    shape = shape,
                    ambientColor = MaterialTheme.colorScheme.scrim,
                    spotColor = MaterialTheme.colorScheme.scrim
                ),
            shape = shape,
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            ExpenseTileContent(
                expense = expense,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 13.dp)
            )
        }
    } else {
        ExpenseTileContent(
            expense = expense,
            modifier = modifier
                .fillMaxWidth()
                .background(Color.White)
                .clickable { onClick(expense) }
                .padding(horizontal = 14.dp, vertical = 13.dp)
        )
    }
}

@Composable
private fun ExpenseTileContent(
    expense: Expense,
    modifier: Modifier = Modifier
) {
    val category = expense.category
    val status = expenseStatus(expense.isPaid)

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(category.color.copy(alpha = 0.12f), CircleShape)
                .border(1.dp, category.color.copy(alpha = 0.32f), CircleShape)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(category.iconRes),
                contentDescription = stringResource(category.labelRes),
                tint = category.color
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = categoryLabel(category),
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = expense.subCategory.ifBlank { stringResource(R.string.expenses_no_details) },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ExpenseStatusTag(status = status)
                Text(
                    text = expense.date,
                    modifier = Modifier.weight(1f, fill = false),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Surface(
            shape = RoundedCornerShape(999.dp),
            color = category.color.copy(alpha = 0.13f)
        ) {
            Text(
                text = stringResource(R.string.currency_amount, expense.amount),
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = category.color,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun ExpenseStatusTag(status: ExpenseTileStatus) {
    Surface(
        shape = RoundedCornerShape(999.dp),
        color = status.color.copy(alpha = 0.13f)
    ) {
        Text(
            text = stringResource(status.labelRes),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = status.color,
            maxLines = 1
        )
    }
}

@Composable
private fun categoryLabel(category: ExpenseCategory): String {
    return when (category) {
        ExpenseCategory.FOOD -> stringResource(R.string.category_food_dining)
        else -> stringResource(category.labelRes)
    }
}

private data class ExpenseTileStatus(
    @StringRes val labelRes: Int,
    val color: Color
)

private fun expenseStatus(isPaid: Boolean): ExpenseTileStatus {
    return if (isPaid) {
        ExpenseTileStatus(
            labelRes = R.string.expense_status_paid,
            color = BudgetSafe
        )
    } else {
        ExpenseTileStatus(
            labelRes = R.string.expense_status_planned,
            color = Color(0xFFF09537)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ExpenseTilePreview() {
    ExpenseTile(
        expense = Expense(
            id = "1",
            category = ExpenseCategory.FOOD,
            subCategory = "Dinner",
            amount = 28.40,
            date = "9 Mar 2026",
            isPaid = false
        ),
        modifier = Modifier.padding(16.dp)
    )
}
