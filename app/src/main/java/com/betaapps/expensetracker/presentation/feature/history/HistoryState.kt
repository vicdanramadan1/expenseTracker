package com.betaapps.expensetracker.presentation.feature.history

import androidx.annotation.StringRes
import com.betaapps.expensetracker.R
import com.betaapps.expensetracker.presentation.feature.home.model.Expense

enum class HistoryStatusFilter(@StringRes val labelRes: Int) {
    ALL(R.string.home_filter_all),
    PAID(R.string.home_filter_paid),
    PLANNED(R.string.home_filter_planned)
}

data class HistoryState(
    val expenses: List<Expense> = emptyList(),
    val searchQuery: String = "",
    val selectedMonthMillis: Long = getHistoryMonthStartMillis(System.currentTimeMillis()),
    val selectedStatusFilter: HistoryStatusFilter = HistoryStatusFilter.ALL,
    val totalExpenses: Double = 0.0,
    val isLoading: Boolean = true,
    val isMonthPickerVisible: Boolean = false,
    val error: String? = null
)
