package com.betaapps.expensetracker.presentation.feature.home.model

data class Expense(
    val id: String,
    val category: ExpenseCategory,
    val subCategory: String,
    val amount: Double,
    val date: String,
    val isPaid: Boolean = true
)
