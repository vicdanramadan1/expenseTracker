package com.betaapps.expensetracker.presentation.feature.addeditexpense

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.betaapps.expensetracker.domain.usecases.AddExpensUsecase
import com.betaapps.expensetracker.domain.usecases.GetExpenseUsecase
import com.betaapps.expensetracker.presentation.feature.home.mapper.toDomain
import com.betaapps.expensetracker.presentation.feature.home.mapper.toUi
import com.betaapps.expensetracker.presentation.feature.home.model.Expense
import com.betaapps.expensetracker.presentation.feature.home.model.ExpenseCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import java.util.UUID

@HiltViewModel
class AddEditExpenseViewModel @Inject constructor(
    private val addExpensUsecase: AddExpensUsecase,
    private val getExpenseUsecase: GetExpenseUsecase,
) : ViewModel() {

    private val _state = MutableStateFlow(AddEditExpenseState())
    val state: StateFlow<AddEditExpenseState> = _state.asStateFlow()

    fun loadExpense(expenseId: String?) {
        val id = expenseId?.toLongOrNull()
        if (id == null) {
            _state.value = AddEditExpenseState()
            return
        }

        _state.value = AddEditExpenseState(expenseId = expenseId)

        viewModelScope.launch {
            val expense = getExpenseUsecase(id)?.toUi()
            _state.value = if (expense == null) {
                AddEditExpenseState(expenseId = expenseId)
            } else {
                AddEditExpenseState(
                    expenseId = expense.id,
                    amountInput = expense.amount.toString(),
                    selectedCategory = expense.category,
                    subCategory = expense.subCategory,
                    date = expense.date
                )
            }
        }
    }

    fun onAmountChange(value: String) {
        _state.value = _state.value.copy(amountInput = value)
    }

    fun onCategoryExpandedChange(expanded: Boolean) {
        _state.value = _state.value.copy(isCategoryExpanded = expanded)
    }

    fun onCategorySelected(category: ExpenseCategory) {
        _state.value = _state.value.copy(
            selectedCategory = category,
            isCategoryExpanded = false
        )
    }

    fun onSubCategoryChange(value: String) {
        _state.value = _state.value.copy(subCategory = value)
    }

    fun onDateChange(value: String) {
        _state.value = _state.value.copy(date = value)
    }

    fun saveExpense() {
        val current = _state.value
        val amount = current.amountInput.toDoubleOrNull() ?: 0.0
        val expense = Expense(
            id = current.expenseId ?: UUID.randomUUID().toString(),
            category = current.selectedCategory,
            subCategory = current.subCategory.trim(),
            amount = amount,
            date = current.date.trim()
        )

        viewModelScope.launch {
            addExpensUsecase(expense.toDomain())
        }
    }
}