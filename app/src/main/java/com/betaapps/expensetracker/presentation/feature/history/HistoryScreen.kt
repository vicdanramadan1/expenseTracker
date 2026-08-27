package com.betaapps.expensetracker.presentation.feature.history

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.betaapps.expensetracker.R
import com.betaapps.expensetracker.presentation.common.Chip
import com.betaapps.expensetracker.presentation.feature.home.components.SwipeableExpenseTile
import com.betaapps.expensetracker.presentation.feature.home.model.Expense
import com.betaapps.expensetracker.presentation.feature.home.model.ExpenseCategory
import com.betaapps.expensetracker.ui.theme.BorderSubtle
import com.betaapps.expensetracker.ui.theme.BudgetSafe
import com.betaapps.expensetracker.ui.theme.ExpenseTrackerTheme
import com.betaapps.expensetracker.ui.theme.TextSecondary

@Composable
fun HistoryScreen(
    state: HistoryState,
    onSearchQueryChange: (String) -> Unit,
    onStatusFilterSelected: (HistoryStatusFilter) -> Unit,
    onMonthFieldClick: () -> Unit,
    onExpenseClick: (Expense) -> Unit,
    modifier: Modifier = Modifier
) {
    val monthLabel = remember(state.selectedMonthMillis) {
        formatHistoryMonthLabel(state.selectedMonthMillis)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAF8)),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        MonthSelectorButton(
            text = monthLabel,
            onClick = onMonthFieldClick
        )
        HistorySearchRow(
            query = state.searchQuery,
            onQueryChange = onSearchQueryChange
        )

        HistoryStatusFilters(
            selectedFilter = state.selectedStatusFilter,
            onFilterSelected = onStatusFilterSelected
        )

        when {
            state.isLoading -> HistoryLoading()
            state.error != null -> HistoryMessageCard(
                title = stringResource(R.string.history_error_title),
                message = state.error.ifBlank { stringResource(R.string.history_error_message) }
            )
            state.expenses.isEmpty() -> HistoryMessageCard(
                title = stringResource(R.string.history_empty_title),
                message = stringResource(R.string.history_empty_message)
            )
            else -> HistoryExpenseContent(
                expenses = state.expenses,
                totalExpenses = state.totalExpenses,
                onExpenseClick = onExpenseClick
            )
        }
    }
}

@Composable
private fun MonthSelectorButton(
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .height(52.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White)
            .border(1.dp, BorderSubtle, RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            imageVector = Icons.Default.DateRange,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(22.dp)
        )
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleMedium,
            fontSize = 14.sp,
            lineHeight = 18.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1
        )
        Icon(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = stringResource(R.string.history_select_month_content_description),
            tint = TextSecondary,
            modifier = Modifier.size(22.dp)
        )
    }
}

@Composable
private fun HistorySearchRow(
    query: String,
    onQueryChange: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 14.sp,
                lineHeight = 18.sp,
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier
                .weight(1f)
                .height(52.dp),
            placeholder = {
                Text(
                    text = stringResource(R.string.history_search_placeholder),
                    color = TextSecondary,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 14.sp,
                    lineHeight = 18.sp,
                    maxLines = 1
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(24.dp)
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(26.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BorderSubtle,
                unfocusedBorderColor = BorderSubtle,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                cursorColor = BudgetSafe
            )
        )

        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(Color.White)
                .border(1.dp, BorderSubtle, RoundedCornerShape(18.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.FilterList,
                contentDescription = stringResource(R.string.history_filter_content_description),
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun HistoryStatusFilters(
    selectedFilter: HistoryStatusFilter,
    onFilterSelected: (HistoryStatusFilter) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HistoryStatusFilter.entries.forEach { filter ->
            Chip(
                label = stringResource(filter.labelRes),
                selected = selectedFilter == filter,
                onClick = { onFilterSelected(filter) }
            )
        }
    }
}

@Composable
private fun HistoryExpenseContent(
    expenses: List<Expense>,
    totalExpenses: Double,
    onExpenseClick: (Expense) -> Unit
) {
    val groups = remember(expenses) { groupHistoryExpenses(expenses) }

    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        groups.forEach { group ->
            HistoryExpenseSection(
                group = group,
                onExpenseClick = onExpenseClick
            )
        }

        HistoryTotalCard(totalExpenses = totalExpenses)
    }
}

@Composable
private fun HistoryExpenseSection(
    group: HistoryDateGroup,
    onExpenseClick: (Expense) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = historySectionTitle(group.dayStartMillis),
            color = TextSecondary,
            style = MaterialTheme.typography.titleMedium,
            fontSize = 14.sp,
            lineHeight = 18.sp,
            fontWeight = FontWeight.SemiBold
        )

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            border = BorderStroke(1.dp, BorderSubtle.copy(alpha = 0.86f))
        ) {
            Column {
                group.expenses.forEachIndexed { index, expense ->
                    SwipeableExpenseTile(
                        expense = expense,
                        showContainer = false,
                        onExpenseClicked = onExpenseClick,
                        onExpenseSwipedToDelete = {}
                    )
                    if (index != group.expenses.lastIndex) {
                        HorizontalDivider(color = BorderSubtle.copy(alpha = 0.72f))
                    }
                }
            }
        }
    }
}

@Composable
private fun HistoryTotalCard(totalExpenses: Double) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(92.dp),
        shape = RoundedCornerShape(22.dp),
        color = Color.White,
        border = BorderStroke(1.dp, BorderSubtle.copy(alpha = 0.86f))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.history_total_expenses),
                color = TextSecondary,
                style = MaterialTheme.typography.titleMedium,
                fontSize = 13.sp,
                lineHeight = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = stringResource(R.string.currency_amount, totalExpenses),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleLarge,
                fontSize = 18.sp,
                lineHeight = 22.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

@Composable
private fun HistoryLoading() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = BudgetSafe)
    }
}

@Composable
private fun HistoryMessageCard(
    title: String,
    message: String
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 180.dp),
        shape = RoundedCornerShape(22.dp),
        color = Color.White,
        border = BorderStroke(1.dp, BorderSubtle.copy(alpha = 0.86f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium,
                fontSize = 15.sp,
                lineHeight = 19.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = message,
                color = TextSecondary,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 13.sp,
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
private fun historySectionTitle(dayStartMillis: Long): String {
    val nowMillis = remember { System.currentTimeMillis() }
    return when {
        isHistorySameDay(dayStartMillis, nowMillis) -> stringResource(R.string.history_today)
        isHistoryYesterday(dayStartMillis, nowMillis) -> stringResource(R.string.history_yesterday)
        else -> formatHistoryDateLabel(dayStartMillis)
    }
}

@Composable
private fun historyCategoryLabel(category: ExpenseCategory): String {
    return when (category) {
        ExpenseCategory.FOOD -> stringResource(R.string.category_food_dining)
        else -> stringResource(category.labelRes)
    }
}

private data class HistoryDateGroup(
    val dayStartMillis: Long,
    val expenses: List<Expense>
)

private fun groupHistoryExpenses(expenses: List<Expense>): List<HistoryDateGroup> {
    return expenses
        .groupBy { expense ->
            val millis = parseHistoryExpenseDateToMillis(expense.date) ?: 0L
            getHistoryDayStartMillis(millis)
        }
        .map { (dayStartMillis, dayExpenses) ->
            HistoryDateGroup(
                dayStartMillis = dayStartMillis,
                expenses = dayExpenses.sortedByDescending {
                    parseHistoryExpenseDateToMillis(it.date) ?: Long.MIN_VALUE
                }
            )
        }
        .sortedByDescending(HistoryDateGroup::dayStartMillis)
}

@Preview(showBackground = true)
@Composable
private fun HistoryScreenPreview() {
    ExpenseTrackerTheme {
        HistoryScreen(
            state = HistoryState(
                isLoading = false,
                expenses = listOf(
                    Expense(
                        id = "1",
                        category = ExpenseCategory.FOOD,
                        subCategory = "Lunch at Cafe",
                        amount = 12.50,
                        date = "12 Jun 2026",
                        isPaid = true
                    ),
                    Expense(
                        id = "2",
                        category = ExpenseCategory.TRANSPORT,
                        subCategory = "Uber Ride",
                        amount = 8.20,
                        date = "12 Jun 2026",
                        isPaid = true
                    ),
                    Expense(
                        id = "3",
                        category = ExpenseCategory.SHOPPING,
                        subCategory = "Groceries",
                        amount = 45.80,
                        date = "11 Jun 2026",
                        isPaid = false
                    )
                ),
                totalExpenses = 66.50
            ),
            onSearchQueryChange = {},
            onStatusFilterSelected = {},
            onMonthFieldClick = {},
            onExpenseClick = {}
        )
    }
}
