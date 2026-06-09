package com.betaapps.expensetracker.presentation.feature.home

import com.betaapps.expensetracker.presentation.feature.home.model.Expense

data class HomeState(
    val data: List<Expense>? = emptyList(),
    val isLoading: Boolean = false,
    val error : String? = null,
    val searchQuery: String = "",
    val selectedMonthMillis: Long = System.currentTimeMillis(),
    val isMonthPickerVisible: Boolean = false
)
