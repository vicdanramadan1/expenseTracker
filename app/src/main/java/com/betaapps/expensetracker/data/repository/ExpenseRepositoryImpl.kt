package com.betaapps.expensetracker.data.repository

import com.betaapps.expensetracker.data.local.dao.ExpenseDao
import com.betaapps.expensetracker.data.local.entity.ExpenseEntity
import com.betaapps.expensetracker.data.mapper.toDomain
import com.betaapps.expensetracker.data.mapper.toEntity
import com.betaapps.expensetracker.domain.model.Expense
import com.betaapps.expensetracker.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ExpenseRepositoryImpl(private val expenseDao: ExpenseDao) : ExpenseRepository {
    override suspend fun getExpenses(): Flow<List<Expense>> =
        expenseDao.getExpenses().map { it.map(ExpenseEntity::toDomain) }

    override suspend fun getExpenseHistory(): Flow<List<Expense>> =
        expenseDao.getExpenses().map { it.map(ExpenseEntity::toDomain) }

    override suspend fun searchExpenses(query: String): Flow<List<Expense>> =
        expenseDao.searchExpenses(query).map { it.map(ExpenseEntity::toDomain) }

    override suspend fun getExpense(id: Long): Expense? =
        expenseDao.getExpenseById(id)?.toDomain()

    override suspend fun addExpense(expense: Expense) {
        expenseDao.addExpense(expense.toEntity())
    }

    override suspend fun deleteExpense(expense: Expense) {
        expenseDao.deleteExpense(expense.toEntity())
    }
}
