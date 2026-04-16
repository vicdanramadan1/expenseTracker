package com.betaapps.expensetracker.presentation.feature.addeditexpense

import com.betaapps.expensetracker.presentation.feature.home.model.ExpenseCategory

data class AddEditExpenseState(
    val expenseId: String? = null,
    val amountInput: String = "",
    val selectedCategory: ExpenseCategory = ExpenseCategory.OTHER,
    val subCategory: String = "",
    val isCategoryExpanded: Boolean = false,
    val selectedDateMillis: Long? = null,
    val isDatePickerVisible: Boolean = false,
    val isSaving: Boolean = false
)
