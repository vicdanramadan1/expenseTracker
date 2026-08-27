package com.betaapps.expensetracker.presentation.feature.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.betaapps.expensetracker.R
import com.betaapps.expensetracker.presentation.common.Chip
import com.betaapps.expensetracker.presentation.common.MonthPicker
import com.betaapps.expensetracker.presentation.feature.home.components.BudgetSummary
import com.betaapps.expensetracker.presentation.feature.home.components.SwipeableExpenseTile
import com.betaapps.expensetracker.presentation.feature.home.mapper.toBudgetSummaryUiState
import com.betaapps.expensetracker.presentation.feature.home.model.Expense
import com.betaapps.expensetracker.presentation.model.BudgetStatus
import com.betaapps.expensetracker.ui.theme.BorderSubtle
import com.betaapps.expensetracker.ui.theme.BudgetDanger
import com.betaapps.expensetracker.ui.theme.BudgetSafe
import com.betaapps.expensetracker.ui.theme.BudgetWarning
import com.betaapps.expensetracker.ui.theme.ExpenseTrackerTheme
import com.betaapps.expensetracker.ui.theme.TextSecondary

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
        BudgetStatus.SAFE -> R.string.budget_on_track_short
        BudgetStatus.WARNING -> R.string.budget_near_limit_short
        BudgetStatus.DANGER -> R.string.budget_exceeded_short
    }

    if (homeState.isMonthPickerVisible) {
        MonthPicker(
            selectedMonthMillis = homeState.selectedMonthMillis,
            onMonthSelected = viewModel::onMonthSelected,
            onDismiss = viewModel::onMonthPickerDismiss
        )
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        BudgetSummary(
            modifier = Modifier.fillMaxWidth(),
            spentAmount = budgetSummary.spentAmount,
            budgetAmount = budgetSummary.budgetAmount,
            statusColor = statusColor,
            selectedMonthText = selectedMonthLabel,
            statusChipText = statusChipText,
            onMonthClick = viewModel::onMonthFieldClick,
        )

        RecentExpensesCard(
            expenses = expenses,
            isLoading = homeState.isLoading,
            onAddExpenseClick = onAddExpenseClick,
            onFilterClick = onSearchClick,
            onViewAllClick = onSearchClick,
            onExpenseClick = onExpenseClick,
            onExpenseSwipedToDelete = viewModel::removeExpense
        )
    }
}

@Composable
private fun RecentExpensesCard(
    expenses: List<Expense>,
    isLoading: Boolean,
    onAddExpenseClick: () -> Unit,
    onFilterClick: () -> Unit,
    onViewAllClick: () -> Unit,
    onExpenseClick: (Expense) -> Unit,
    onExpenseSwipedToDelete: (Expense) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 14.dp,
                shape = RoundedCornerShape(22.dp),
                ambientColor = Color(0x15111827),
                spotColor = Color(0x1F111827)
            ),
        shape = RoundedCornerShape(22.dp),
        border = BorderStroke(1.dp, BorderSubtle.copy(alpha = 0.72f)),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.home_recent_expenses),
                    style = MaterialTheme.typography.headlineSmall,
                    fontSize = 20.sp,
                    lineHeight = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                TextButton(
                    onClick = onAddExpenseClick,
                    colors = ButtonDefaults.textButtonColors(contentColor = BudgetSafe)
                ) {
                    Text(
                        text = stringResource(R.string.home_add_expense_button),
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 15.sp,
                        lineHeight = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Chip(
                    label = stringResource(R.string.home_filter_all),
                    selected = true,
                    modifier = Modifier.weight(1f),
                    horizontalPadding = 8.dp
                )
                Chip(
                    label = stringResource(R.string.home_filter_paid),
                    selected = false,
                    modifier = Modifier.weight(1f),
                    horizontalPadding = 8.dp
                )
                Chip(
                    label = stringResource(R.string.home_filter_planned),
                    selected = false,
                    modifier = Modifier.weight(1f),
                    horizontalPadding = 8.dp
                )
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .border(1.dp, BorderSubtle, CircleShape)
                        .clickable(onClick = onFilterClick),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = stringResource(R.string.home_filter_content_description),
                        tint = TextSecondary,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            when {
                isLoading -> LoadingExpenses()
                expenses.isEmpty() -> EmptyExpenses()
                else -> RecentExpensesList(
                    expenses = expenses.take(3),
                    onExpenseClick = onExpenseClick,
                    onExpenseSwipedToDelete = onExpenseSwipedToDelete
                )
            }

            TextButton(
                onClick = onViewAllClick,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.textButtonColors(contentColor = BudgetSafe)
            ) {
                Text(
                    text = stringResource(R.string.home_view_all_expenses),
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 14.sp,
                    lineHeight = 18.sp,
                    fontWeight = FontWeight.Medium
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun LoadingExpenses() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .border(1.dp, BorderSubtle, RoundedCornerShape(20.dp)),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = BudgetSafe)
    }
}

@Composable
private fun EmptyExpenses() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .border(1.dp, BorderSubtle, RoundedCornerShape(20.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.home_month_expenses_empty),
            color = TextSecondary,
            style = MaterialTheme.typography.titleMedium,
            fontSize = 14.sp,
            lineHeight = 18.sp
        )
    }
}

@Composable
private fun RecentExpensesList(
    expenses: List<Expense>,
    onExpenseClick: (Expense) -> Unit,
    onExpenseSwipedToDelete: (Expense) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, BorderSubtle, RoundedCornerShape(20.dp))
    ) {
        expenses.forEachIndexed { index, expense ->
            SwipeableExpenseTile(
                expense = expense,
                showContainer = false,
                onExpenseClicked = onExpenseClick,
                onExpenseSwipedToDelete = onExpenseSwipedToDelete
            )

            if (index != expenses.lastIndex) {
                HorizontalDivider(color = BorderSubtle)
            }
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
