package com.betaapps.expensetracker.presentation.home

import com.betaapps.expensetracker.presentation.home.model.Expense

data class HomeState(
    val data: List<Expense>? = emptyList(),
    val isLoading: Boolean = false,
    val error : String? = null
)
