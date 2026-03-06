package com.betaapps.expensetracker.domain.usecases

import com.betaapps.expensetracker.domain.model.Expense
import com.betaapps.expensetracker.domain.repository.ExpenseRepository

class DeleteExpenseUsecase(private val repository: ExpenseRepository) {
    suspend operator fun invoke(expense: Expense)
    {
        repository.deleteExpense(expense)
    }
}