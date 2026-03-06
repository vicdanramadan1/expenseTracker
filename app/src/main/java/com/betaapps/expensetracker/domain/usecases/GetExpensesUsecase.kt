package com.betaapps.expensetracker.domain.usecases

import com.betaapps.expensetracker.domain.repository.ExpenseRepository

class GetExpensesUsecase(private val repository: ExpenseRepository) {
    suspend operator fun invoke()
    {
        repository.getExpenses()
    }
}