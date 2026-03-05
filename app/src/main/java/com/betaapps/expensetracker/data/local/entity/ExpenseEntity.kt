package com.betaapps.expensetracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
class ExpenseEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val amount: Double,
    val category: String,
    val description: String?,
    val date: Long
)