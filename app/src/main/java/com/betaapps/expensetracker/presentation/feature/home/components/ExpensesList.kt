package com.betaapps.expensetracker.presentation.feature.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.betaapps.expensetracker.presentation.feature.home.model.Expense
import com.betaapps.expensetracker.presentation.feature.home.model.ExpenseCategory

@Composable
fun ExpensesList(
    expenses: List<Expense>?,
    modifier: Modifier = Modifier,
    onExpenseClicked: (Expense) -> Unit = {}
) {
    val listState = rememberLazyListState()
    val firstVisibleIndex by remember { derivedStateOf { listState.firstVisibleItemIndex } }
    val isScrolling by remember { derivedStateOf { listState.isScrollInProgress } }

    LazyColumn(
        modifier = modifier,
        state = listState,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        if (expenses != null)
        itemsIndexed(expenses) { index, expense ->
            val alpha = if (index == firstVisibleIndex) {
                animateFloatAsState(
                    targetValue = if (isScrolling) 0.5f else 1f,
                    animationSpec = tween(durationMillis = 180),
                    label = "first_tile_alpha"
                ).value
            } else {
                1f
            }

            ExpenseTile(
                expense = expense,
                modifier = Modifier.graphicsLayer(alpha = alpha),
                onClick = {
                    onExpenseClicked(expense)
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ExpensesListPreview() {
    ExpensesList(
        expenses = listOf(
            Expense(
                id = "",
                category = ExpenseCategory.FOOD,
                amount = 20.0,
                date = "20 Apr 2025",
                subCategory = ""
            ),
            Expense(
                id = "",
                category = ExpenseCategory.FOOD,
                amount = 20.0,
                date = "20 Apr 2025",
                subCategory = ""
            ),
            Expense(
                id = "",
                category =ExpenseCategory.FOOD,
                amount = 20.0,
                date = "20 Apr 2025",
                subCategory = ""
            )
        ),
        onExpenseClicked = {}
    )
}
