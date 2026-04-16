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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import javax.inject.Inject

sealed interface AddEditExpenseUiEvent {
    data object SaveSuccess : AddEditExpenseUiEvent
}

@HiltViewModel
class AddEditExpenseViewModel @Inject constructor(
    private val addExpensUsecase: AddExpensUsecase,
    private val getExpenseUsecase: GetExpenseUsecase,
) : ViewModel() {

    private val _state = MutableStateFlow(AddEditExpenseState())
    val state: StateFlow<AddEditExpenseState> = _state.asStateFlow()
    private val _uiEvents = MutableSharedFlow<AddEditExpenseUiEvent>()
    val uiEvents: SharedFlow<AddEditExpenseUiEvent> = _uiEvents.asSharedFlow()
    private val datePattern = "d MMM yyyy"

    val isFormValid: StateFlow<Boolean> = _state.map { state ->
        with(state) {
            amountInput.trim().isNotBlank() &&
                    selectedDateMillis != null &&
                    !isSaving
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false)

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
                    selectedDateMillis = parseDateToMillis(expense.date)
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

    fun onDateFieldClick() {
        _state.value = _state.value.copy(isDatePickerVisible = true)
    }

    fun onDatePickerDismiss() {
        _state.value = _state.value.copy(isDatePickerVisible = false)
    }

    fun onDateSelected(dateMillis: Long) {
        _state.value = _state.value.copy(
            selectedDateMillis = dateMillis,
            isDatePickerVisible = false
        )
    }

    fun saveExpense() {
        val current = _state.value
        if (current.isSaving) return
        val selectedDateMillis = current.selectedDateMillis ?: return
        val amount = current.amountInput.toDoubleOrNull() ?: 0.0
        val expense = Expense(
            id = current.expenseId ?: UUID.randomUUID().toString(),
            category = current.selectedCategory,
            subCategory = current.subCategory.trim(),
            amount = amount,
            date = formatDate(selectedDateMillis)
        )

        _state.value = current.copy(isSaving = true)

        viewModelScope.launch {
            val saved = runCatching { addExpensUsecase(expense.toDomain()) }.isSuccess
            _state.value = _state.value.copy(isSaving = false)
            if (saved) {
                _uiEvents.emit(AddEditExpenseUiEvent.SaveSuccess)
            }
        }
    }

    private fun parseDateToMillis(rawDate: String): Long? {
        if (rawDate.isBlank()) return null
        return runCatching {
            val formatter = SimpleDateFormat(datePattern, Locale.getDefault())
            formatter.parse(rawDate)?.time
        }.getOrNull()
    }

    private fun formatDate(dateMillis: Long): String {
        val formatter = SimpleDateFormat(datePattern, Locale.getDefault())
        return formatter.format(Date(dateMillis))
    }
}
