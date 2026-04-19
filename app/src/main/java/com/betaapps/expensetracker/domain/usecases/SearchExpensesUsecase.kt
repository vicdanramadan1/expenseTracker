package com.betaapps.expensetracker.domain.usecases

import com.betaapps.expensetracker.domain.model.Expense
import com.betaapps.expensetracker.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow

class SearchExpensesUsecase(private val repository: ExpenseRepository) {
    suspend operator fun invoke(query: String): Flow<List<Expense>> =
        repository.searchExpenses(query)
}