package com.betaapps.expensetracker.presentation.feature.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.betaapps.expensetracker.R
import com.betaapps.expensetracker.presentation.feature.home.components.BudgetSummary
import com.betaapps.expensetracker.presentation.feature.home.components.SwipeableExpenseTile
import com.betaapps.expensetracker.presentation.feature.home.mapper.toBudgetSummaryUiState
import com.betaapps.expensetracker.presentation.feature.home.model.Expense
import com.betaapps.expensetracker.presentation.model.BudgetStatus
import com.betaapps.expensetracker.ui.theme.BudgetDanger
import com.betaapps.expensetracker.ui.theme.BudgetWarning
import com.betaapps.expensetracker.ui.theme.BudgetSafe
import com.betaapps.expensetracker.ui.theme.ExpenseTrackerTheme
import java.util.Calendar

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
    val selectedMonthLabel = remember(homeState.selectedMonthMillis) {
        formatMonthLabel(homeState.selectedMonthMillis)
    }
    val budgetSummary = remember(expenses) {
        expenses.toBudgetSummaryUiState()
    }
    val statusColor = when (budgetSummary.budgetStatus) {
        BudgetStatus.SAFE -> BudgetSafe
        BudgetStatus.WARNING -> BudgetWarning
        BudgetStatus.DANGER -> BudgetDanger
    }
    val statusChipText = when (budgetSummary.budgetStatus) {
        BudgetStatus.SAFE -> R.string.budget_on_track
        BudgetStatus.WARNING -> R.string.budget_near_limit
        BudgetStatus.DANGER -> R.string.budget_excided
    }
    val topSpending = budgetSummary.topSpendingCategory?.let { stringResource(it.labelRes) }
        ?: stringResource(R.string.budget_top_spending_none)

    if (homeState.isMonthPickerVisible) {
        MonthPickerDialog(
            selectedMonthMillis = homeState.selectedMonthMillis,
            onMonthSelected = viewModel::onMonthSelected,
            onDismiss = viewModel::onMonthPickerDismiss
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        BudgetSummary(
            statusChipText = statusChipText,
            modifier = Modifier,
            spentAmount = budgetSummary.spentAmount,
            budgetAmount = budgetSummary.budgetAmount,
            statusColor = statusColor,
            topSpending = topSpending,
            selectedMonthText = selectedMonthLabel,
            onEditBudgetClick = {},
            onMonthClick = viewModel::onMonthFieldClick,
        )


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.home_selected_month_expenses, selectedMonthLabel),
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

        when {
            homeState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            expenses.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.home_month_expenses_empty),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            else -> {
                expenses.forEach { expense ->
                    SwipeableExpenseTile(
                        expense = expense,
                        onExpenseClicked = onExpenseClick,
                        onExpenseSwipedToDelete = viewModel::removeExpense
                    )
                }
            }
        }
    }
}

@Composable
private fun MonthPickerDialog(
    selectedMonthMillis: Long,
    onMonthSelected: (year: Int, month: Int) -> Unit,
    onDismiss: () -> Unit
) {
    val selectedCalendar = remember(selectedMonthMillis) {
        Calendar.getInstance().apply { timeInMillis = selectedMonthMillis }
    }
    val selectedYear = selectedCalendar.get(Calendar.YEAR)
    val selectedMonth = selectedCalendar.get(Calendar.MONTH)
    var visibleYear by remember(selectedMonthMillis) { mutableIntStateOf(selectedYear) }
    val monthNames = remember { getShortMonthNames() }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(R.string.budget_month_picker_title))
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { visibleYear -= 1 }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = stringResource(R.string.budget_previous_year_content_description)
                        )
                    }
                    Text(
                        text = visibleYear.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    IconButton(onClick = { visibleYear += 1 }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = stringResource(R.string.budget_next_year_content_description)
                        )
                    }
                }

                monthNames.chunked(3).forEachIndexed { rowIndex, rowMonths ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        rowMonths.forEachIndexed { columnIndex, monthName ->
                            val monthIndex = rowIndex * 3 + columnIndex
                            val isSelected = visibleYear == selectedYear && monthIndex == selectedMonth
                            TextButton(
                                onClick = { onMonthSelected(visibleYear, monthIndex) },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 1.dp),
                                colors = ButtonDefaults.textButtonColors(
                                    containerColor = if (isSelected) {
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                                    } else {
                                        Color.Transparent
                                    },
                                    contentColor = if (isSelected) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        MaterialTheme.colorScheme.onSurface
                                    }
                                )
                            ) {
                                Text(
                                    text = monthName,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.action_cancel))
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    ExpenseTrackerTheme {
        HomeScreen(onAddExpenseClick = {})
    }
}
