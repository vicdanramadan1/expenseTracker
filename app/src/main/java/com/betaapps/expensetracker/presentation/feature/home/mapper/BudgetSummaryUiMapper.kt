package com.betaapps.expensetracker.presentation.feature.home.mapper

import com.betaapps.expensetracker.presentation.feature.home.BudgetSummaryUiState
import com.betaapps.expensetracker.presentation.feature.home.model.Expense
import com.betaapps.expensetracker.presentation.model.BudgetStatus

private const val DEFAULT_MONTHLY_BUDGET_AMOUNT = 2_000.0
private const val WARNING_BUDGET_THRESHOLD = 0.8

fun List<Expense>.toBudgetSummaryUiState(
    budgetAmount: Double = DEFAULT_MONTHLY_BUDGET_AMOUNT
): BudgetSummaryUiState {
    val spentAmount = sumOf { it.amount }
    val topSpendingCategory = groupBy { it.category }
        .maxByOrNull { (_, expenses) -> expenses.sumOf { it.amount } }
        ?.key

    return BudgetSummaryUiState(
        spentAmount = spentAmount,
        budgetAmount = budgetAmount,
        budgetStatus = getBudgetStatus(spentAmount, budgetAmount),
        topSpendingCategory = topSpendingCategory
    )
}

private fun getBudgetStatus(spentAmount: Double, budgetAmount: Double): BudgetStatus = when {
    spentAmount >= budgetAmount -> BudgetStatus.DANGER
    spentAmount >= budgetAmount * WARNING_BUDGET_THRESHOLD -> BudgetStatus.WARNING
    else -> BudgetStatus.SAFE
}