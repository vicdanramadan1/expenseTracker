package com.betaapps.expensetracker.presentation.feature.home

import com.betaapps.expensetracker.presentation.feature.home.model.Expense

sealed interface HomeScreenEvent {
    data object LoadExpenses : HomeScreenEvent
    data class AddNewExpenseClicked(val expense: Expense) : HomeScreenEvent
    data class ExpenseTileClicked(val expense: Expense) : HomeScreenEvent
}
