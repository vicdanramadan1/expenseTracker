package com.betaapps.expensetracker.presentation.feature.history

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.betaapps.expensetracker.R
import com.betaapps.expensetracker.presentation.common.ExpenseTopAppBar
import com.betaapps.expensetracker.presentation.common.ExpenseTopAppBarIconButton
import com.betaapps.expensetracker.presentation.feature.home.components.ExpenseTile
import com.betaapps.expensetracker.presentation.feature.home.model.Expense
import com.betaapps.expensetracker.presentation.feature.home.model.ExpenseCategory
import com.betaapps.expensetracker.ui.theme.BorderSubtle
import com.betaapps.expensetracker.ui.theme.BudgetSafe
import com.betaapps.expensetracker.ui.theme.BudgetWarning
import com.betaapps.expensetracker.ui.theme.ExpenseTrackerTheme
import com.betaapps.expensetracker.ui.theme.TextSecondary
import java.util.Calendar

@Composable
fun HistoryScreen(
    state: HistoryState,
    onSearchQueryChange: (String) -> Unit,
    onStatusFilterSelected: (HistoryStatusFilter) -> Unit,
    onMonthFieldClick: () -> Unit,
    onMonthPickerDismiss: () -> Unit,
    onMonthSelected: (year: Int, month: Int) -> Unit,
    onExpenseClick: (Expense) -> Unit,
    modifier: Modifier = Modifier
) {
    val monthLabel = remember(state.selectedMonthMillis) {
        formatHistoryMonthLabel(state.selectedMonthMillis)
    }

    if (state.isMonthPickerVisible) {
        HistoryMonthPickerDialog(
            selectedMonthMillis = state.selectedMonthMillis,
            onMonthSelected = onMonthSelected,
            onDismiss = onMonthPickerDismiss
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAF8))
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp)
            .padding(top = 14.dp, bottom = 26.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {

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
private fun HistoryTopBar(
    selectedMonthText: String,
    onMonthClick: () -> Unit
) {
    ExpenseTopAppBar(
        title = stringResource(R.string.tab_history),
        height = 56.dp,
        horizontalPadding = 0.dp,
        titleStyle = MaterialTheme.typography.headlineMedium.copy(
            fontSize = 28.sp,
            lineHeight = 34.sp,
            fontWeight = FontWeight.ExtraBold
        ),
        navigationIcon = {
            ExpenseTopAppBarIconButton(
                imageVector = Icons.Default.Menu,
                contentDescription = stringResource(R.string.home_menu_content_description),
                onClick = {},
                iconSize = 30.dp,
                buttonSize = 46.dp
            )
        },
        endContent = {
            MonthSelectorButton(
                text = selectedMonthText,
                onClick = onMonthClick
            )
        }
    )
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
            fontSize = 15.sp,
            lineHeight = 19.sp,
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
            modifier = Modifier
                .weight(1f)
                .height(58.dp),
            placeholder = {
                Text(
                    text = stringResource(R.string.history_search_placeholder),
                    color = TextSecondary,
                    maxLines = 1
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(28.dp)
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
                .size(58.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(Color.White)
                .border(1.dp, BorderSubtle, RoundedCornerShape(18.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.FilterList,
                contentDescription = stringResource(R.string.history_filter_content_description),
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(28.dp)
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
            HistoryFilterChip(
                label = stringResource(filter.labelRes),
                selected = selectedFilter == filter,
                onClick = { onFilterSelected(filter) }
            )
        }
    }
}

@Composable
private fun HistoryFilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val contentColor = if (selected) Color.White else MaterialTheme.colorScheme.onSurface
    val borderColor = if (selected) BudgetSafe else BorderSubtle

    Box(
        modifier = Modifier
            .height(44.dp)
            .clip(RoundedCornerShape(999.dp))
            .background(if (selected) BudgetSafe else Color.White)
            .border(1.dp, borderColor, RoundedCornerShape(999.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 22.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = contentColor,
            style = MaterialTheme.typography.titleMedium,
            fontSize = 15.sp,
            lineHeight = 18.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1
        )
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
            fontSize = 16.sp,
            lineHeight = 20.sp,
            fontWeight = FontWeight.SemiBold
        )

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            color = Color.White,
            border = BorderStroke(1.dp, BorderSubtle.copy(alpha = 0.86f))
        ) {
            Column {
                group.expenses.forEachIndexed { index, expense ->
                    ExpenseTile(
                        expense = expense,
                        onClick = { onExpenseClick(expense) }
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
                fontSize = 15.sp,
                lineHeight = 19.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = stringResource(R.string.currency_amount, totalExpenses),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleLarge,
                fontSize = 22.sp,
                lineHeight = 26.sp,
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
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = message,
                color = TextSecondary,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun HistoryMonthPickerDialog(
    selectedMonthMillis: Long,
    onMonthSelected: (year: Int, month: Int) -> Unit,
    onDismiss: () -> Unit
) {
    val selectedCalendar = remember(selectedMonthMillis) {
        Calendar.getInstance().apply { timeInMillis = selectedMonthMillis }
    }
    val selectedYear = selectedCalendar.get(Calendar.YEAR)
    val selectedMonth = selectedCalendar.get(Calendar.MONTH)
    val monthNames = remember { getHistoryShortMonthNames() }
    var visibleYear by remember(selectedMonthMillis) { mutableIntStateOf(selectedYear) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.budget_month_picker_title)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = { visibleYear -= 1 }) {
                        Text(stringResource(R.string.history_previous_year))
                    }
                    Text(
                        text = visibleYear.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    TextButton(onClick = { visibleYear += 1 }) {
                        Text(stringResource(R.string.history_next_year))
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
                                        BudgetSafe.copy(alpha = 0.12f)
                                    } else {
                                        Color.Transparent
                                    },
                                    contentColor = if (isSelected) {
                                        BudgetSafe
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
            onMonthPickerDismiss = {},
            onMonthSelected = { _, _ -> },
            onExpenseClick = {}
        )
    }
}
