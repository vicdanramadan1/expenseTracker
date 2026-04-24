package com.betaapps.expensetracker.presentation.feature.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.betaapps.expensetracker.R
import com.betaapps.expensetracker.presentation.feature.home.components.BudgetSummary
import com.betaapps.expensetracker.presentation.feature.home.components.SwipeableExpenseTile
import com.betaapps.expensetracker.presentation.feature.home.model.Expense
import com.betaapps.expensetracker.ui.theme.BudgetSafe
import com.betaapps.expensetracker.ui.theme.ExpenseTrackerTheme

@SuppressLint("SuspiciousIndentation")
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeScreenViewModel = hiltViewModel(),
    onAddExpenseClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onExpenseClick: (Expense) -> Unit = {}
) {
    val homeState = viewModel.homeState.collectAsState().value
    val expenses = homeState.data.orEmpty()

    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        BudgetSummary(
            statusChipText = R.string.budget_on_track,
            modifier = Modifier,
            spentAmount = 500.0,
            budgetAmount = 2000.0,
            statusColor = BudgetSafe,
            topSpending = "transport",
            onEditBudgetClick = {},
        ) { }


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.home_recent_expenses),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            TextButton(
                onClick = {
                    onAddExpenseClick()
                },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = stringResource(R.string.home_add_expense_button),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }



        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 1.dp)

        OutlinedButton(
            onClick = onSearchClick,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp)
        ) {
            Icon(
                painter = painterResource(android.R.drawable.ic_menu_search),
                contentDescription = null
            )
            Text(text = stringResource(R.string.home_search_open_button))
        }

        expenses.forEach { expense ->

            SwipeableExpenseTile(
                expense = expense,
                onExpenseClicked = onExpenseClick,
                onExpenseSwipedToDelete = viewModel::removeExpense
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    ExpenseTrackerTheme {
        HomeScreen(onAddExpenseClick = {})
    }
}
