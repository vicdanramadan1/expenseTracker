package com.betaapps.expensetracker.domain.repository

import com.betaapps.expensetracker.domain.model.Expense
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {

    suspend fun getExpenses(): Flow<List<Expense>>

    suspend fun getExpense(): Expense?

    suspend fun addExpense(expense: Expense)

    suspend fun deleteExpense(expense: Expense)
}