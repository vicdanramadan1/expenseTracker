package com.betaapps.expensetracker.data.mapper

import com.betaapps.expensetracker.data.local.entity.ExpenseEntity
import com.betaapps.expensetracker.domain.model.Expense

fun Expense.toEntity(): ExpenseEntity = ExpenseEntity(
    id = this.id,
    amount = this.amount,
    category = this.category,
    description = this.description,
    createdAt = this.createdAt,
    isPaid = this.isPaid
)

fun ExpenseEntity.toDomain(): Expense = Expense(
    id = this.id,
    amount = this.amount,
    category = this.category,
    description = this.description,
    createdAt = this.createdAt,
    isPaid = this.isPaid
)
