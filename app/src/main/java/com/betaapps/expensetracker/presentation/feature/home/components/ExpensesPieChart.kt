package com.betaapps.expensetracker.presentation.feature.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.betaapps.expensetracker.presentation.feature.home.model.Expense
import com.betaapps.expensetracker.presentation.feature.home.model.ExpenseCategory

@Composable
fun ExpensePieChart(
    expenses: List<Expense>?,
    modifier: Modifier = Modifier
) {
    val cardShape = RoundedCornerShape(18.dp)

    // Memoize expensive grouping and totals until the list reference changes.

    val slices = remember(expenses) { buildSlices(expenses) }
    val total = remember(slices) { slices.sumOf { it.amount.toDouble() }.toFloat() }

    if (slices.isEmpty() || total <= 0f) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(220.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("No expenses yet")
        }
        return
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 10.dp,
                shape = cardShape,
                ambientColor = MaterialTheme.colorScheme.scrim,
                spotColor = MaterialTheme.colorScheme.scrim
            ),
        shape = cardShape,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PieCanvas(
                slices = slices,
                total = total,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            LegendList(slices = slices, mutedTextColor = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun PieCanvas(
    slices: List<PieSlice>,
    total: Float,
    modifier: Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(190.dp)) {
            val ringWidth = 56f
            var startAngle = -90f

            slices.forEach { slice ->
                val sweep = (slice.amount / total) * 360f
                drawArc(
                    color = slice.color,
                    startAngle = startAngle,
                    sweepAngle = sweep,
                    useCenter = false,
                    style = Stroke(width = ringWidth)
                )
                startAngle += sweep
            }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Total",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
            Text(
                text = "$${"%.2f".format(total)}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun LegendList(
    slices: List<PieSlice>,
    mutedTextColor: Color
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        slices.forEach { slice ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(slice.color, CircleShape)
                )
                Text(
                    text = "${slice.label}: $${"%.2f".format(slice.amount)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = mutedTextColor
                )
            }
        }
    }
}

private fun buildSlices(expenses: List<Expense>?): List<PieSlice> {
    val totalsByCategory = LinkedHashMap<ExpenseCategory, Double>()

    expenses?.forEach { expense ->
        val category = expense.category
        val running = totalsByCategory[category] ?: 0.0
        totalsByCategory[category] = running + expense.amount
    }

    return totalsByCategory
        .map { (category, totalAmount) ->
            PieSlice(
                label = category.label,
                amount = totalAmount.toFloat(),
                color = category.color
            )
        }
        .sortedByDescending { it.amount }
}

private data class PieSlice(
    val label: String,
    val amount: Float,
    val color: Color
)

@Preview(showBackground = true)
@Composable
private fun ExpensePieChartPreview() {
    ExpensePieChart(
        expenses = listOf(
            Expense(
                id = "1",
                category = ExpenseCategory.FOOD,
                subCategory = "Groceries",
                amount = 120.0,
                date = "Mar 8, 2026"
            ),
            Expense(
                id = "2",
                category = ExpenseCategory.TRANSPORT,
                subCategory = "Taxi",
                amount = 70.0,
                date = "Mar 8, 2026"
            ),
            Expense(
                id = "3",
                category = ExpenseCategory.ENTERTAINMENT,
                subCategory = "Movies",
                amount = 45.0,
                date = "Mar 8, 2026"
            )
        ),
        modifier = Modifier.padding(16.dp)
    )
}
