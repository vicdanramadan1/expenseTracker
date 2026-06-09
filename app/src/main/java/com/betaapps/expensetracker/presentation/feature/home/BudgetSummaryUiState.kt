package com.betaapps.expensetracker.presentation.feature.home

import com.betaapps.expensetracker.presentation.feature.home.model.ExpenseCategory
import com.betaapps.expensetracker.presentation.model.BudgetStatus

data class BudgetSummaryUiState(
    val spentAmount: Double,
    val budgetAmount: Double,
    val budgetStatus: BudgetStatus,
    val topSpendingCategory: ExpenseCategory?
)