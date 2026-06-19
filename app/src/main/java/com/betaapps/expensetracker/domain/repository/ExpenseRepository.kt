package com.betaapps.expensetracker.domain.repository

import com.betaapps.expensetracker.domain.model.Expense
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {

    suspend fun getExpenses(): Flow<List<Expense>>

    suspend fun getExpenseHistory(): Flow<List<Expense>>

    suspend fun searchExpenses(query: String): Flow<List<Expense>>

    suspend fun getExpense(id: Long): Expense?

    suspend fun addExpense(expense: Expense)

    suspend fun deleteExpense(expense: Expense)
}
