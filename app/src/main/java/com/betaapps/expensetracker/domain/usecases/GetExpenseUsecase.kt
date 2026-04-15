package com.betaapps.expensetracker.domain.usecases

import com.betaapps.expensetracker.domain.model.Expense
import com.betaapps.expensetracker.domain.repository.ExpenseRepository

class GetExpenseUsecase(private val repository: ExpenseRepository) {
    suspend operator fun invoke(id: Long): Expense? = repository.getExpense(id)
}
