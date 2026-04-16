package com.betaapps.expensetracker.presentation.feature.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import com.betaapps.expensetracker.R
import com.betaapps.expensetracker.presentation.feature.home.model.Expense
import com.betaapps.expensetracker.presentation.feature.home.model.ExpenseCategory

@Composable
fun ExpensesList(
    expenses: List<Expense>?,
    modifier: Modifier = Modifier,
    onExpenseClicked: (Expense) -> Unit,
    onExpenseSwipedToDelete: (Expense) -> Unit
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
        itemsIndexed(items = expenses, key = { _, expense -> expense.id }) { index, expense ->
            val alpha = if (index == firstVisibleIndex) {
                animateFloatAsState(
                    targetValue = if (isScrolling) 0.5f else 1f,
                    animationSpec = tween(durationMillis = 180),
                    label = "first_tile_alpha"
                ).value
            } else {
                1f
            }

            val dismissState = rememberSwipeToDismissBoxState()
            LaunchedEffect(dismissState.currentValue) {
                if (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart) {
                    onExpenseSwipedToDelete(expense)
                }
            }

            SwipeToDismissBox(
                state = dismissState,
                enableDismissFromStartToEnd = false,
                enableDismissFromEndToStart = true,
                backgroundContent = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.error , shape =  RoundedCornerShape(18.dp))
                            .padding(horizontal = 20.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Icon(
                            painter = painterResource(android.R.drawable.ic_menu_delete),
                            contentDescription = stringResource(R.string.expenses_delete_content_description),
                            tint = Color.White
                        )
                    }
                }
            ) {
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
        onExpenseClicked = {},
        onExpenseSwipedToDelete = {}
    )
}
