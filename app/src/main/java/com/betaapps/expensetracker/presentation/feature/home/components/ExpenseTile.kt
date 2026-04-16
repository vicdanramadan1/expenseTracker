package com.betaapps.expensetracker.presentation.feature.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.betaapps.expensetracker.presentation.feature.home.model.Expense
import com.betaapps.expensetracker.presentation.feature.home.model.ExpenseCategory

@Composable
fun ExpenseTile(
    expense: Expense,
    modifier: Modifier = Modifier,
    onClick: (Expense) -> Unit = {}
) {
    val category = expense.category

    Card(
        onClick = { onClick(expense) },
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 10.dp,
                shape =  RoundedCornerShape(18.dp),
                ambientColor = MaterialTheme.colorScheme.scrim,
                spotColor = MaterialTheme.colorScheme.scrim
            ),
        shape =  RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 13.dp),
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
                    contentDescription = category.label,
                    tint = category.color
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = category.label,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = expense.subCategory.ifBlank { "No details" },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = category.color.copy(alpha = 0.13f)
                ) {
                    Text(
                        text = "$${"%.2f".format(expense.amount)}",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = category.color
                    )
                }
                Text(
                    text = expense.date,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
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
            date = "9 Mar 2026"
        ),
        modifier = Modifier.padding(16.dp)
    )
}
