package com.betaapps.expensetracker.presentation.feature.addeditexpense

import com.betaapps.expensetracker.presentation.feature.home.model.ExpenseCategory

data class AddEditExpenseState(
    val expenseId: String? = null,
    val amountInput: String = "",
    val selectedCategory: ExpenseCategory = ExpenseCategory.OTHER,
    val subCategory: String = "",
    val date: String = "",
    val isCategoryExpanded: Boolean = false
)