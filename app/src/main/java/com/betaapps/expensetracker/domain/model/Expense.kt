package com.betaapps.expensetracker.domain.model

data class Expense(
    val id: Long,
    val amount: Double,
    val category: String,
    val description: String?,
    val createdAt: Long,
    val isPaid: Boolean = true
)
