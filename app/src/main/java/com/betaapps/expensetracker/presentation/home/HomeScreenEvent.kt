package com.betaapps.expensetracker.presentation.home

import com.betaapps.expensetracker.presentation.home.model.Expense

sealed interface HomeScreenEvent {
    data object LoadExpenses : HomeScreenEvent
    data class AddNewExpenseClicked(val expense: Expense) : HomeScreenEvent
    data class ExpenseTileClicked(val expense: Expense) : HomeScreenEvent
}
