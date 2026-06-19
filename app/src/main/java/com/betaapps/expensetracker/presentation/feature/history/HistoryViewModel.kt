package com.betaapps.expensetracker.presentation.feature.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.betaapps.expensetracker.domain.usecases.GetExpenseHistoryUsecase
import com.betaapps.expensetracker.presentation.feature.home.mapper.toUiList
import com.betaapps.expensetracker.presentation.feature.home.model.Expense
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getExpenseHistoryUsecase: GetExpenseHistoryUsecase
) : ViewModel() {

    private var allExpenses: List<Expense> = emptyList()

    private val _state = MutableStateFlow(
        HistoryState(
            selectedMonthMillis = getHistoryMonthStartMillis(System.currentTimeMillis())
        )
    )
    val state: StateFlow<HistoryState> = _state.asStateFlow()

    init {
        loadHistory()
    }

    fun onSearchQueryChange(query: String) {
        _state.update { it.copy(searchQuery = query) }
        applyFilters()
    }

    fun onStatusFilterSelected(filter: HistoryStatusFilter) {
        _state.update { it.copy(selectedStatusFilter = filter) }
        applyFilters()
    }

    fun onMonthFieldClick() {
        _state.update { it.copy(isMonthPickerVisible = true) }
    }

    fun onMonthPickerDismiss() {
        _state.update { it.copy(isMonthPickerVisible = false) }
    }

    fun onMonthSelected(year: Int, month: Int) {
        _state.update {
            it.copy(
                selectedMonthMillis = getHistoryMonthStartMillis(year, month),
                isMonthPickerVisible = false
            )
        }
        applyFilters()
    }

    private fun loadHistory() {
        _state.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            runCatching {
                getExpenseHistoryUsecase().collect { expenses ->
                    allExpenses = expenses.toUiList()
                    applyFilters(isLoading = false)
                }
            }.onFailure { throwable ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = throwable.message ?: ""
                    )
                }
            }
        }
    }

    private fun applyFilters(isLoading: Boolean = _state.value.isLoading) {
        val current = _state.value
        val query = current.searchQuery.trim().lowercase(Locale.getDefault())
        val filtered = allExpenses
            .asSequence()
            .filter { expense ->
                val expenseMillis = parseHistoryExpenseDateToMillis(expense.date) ?: return@filter false
                isHistorySameMonth(expenseMillis, current.selectedMonthMillis)
            }
            .filter { expense ->
                when (current.selectedStatusFilter) {
                    HistoryStatusFilter.ALL -> true
                    HistoryStatusFilter.PAID -> expense.isPaid
                    HistoryStatusFilter.PLANNED -> !expense.isPaid
                }
            }
            .filter { expense ->
                if (query.isBlank()) {
                    true
                } else {
                    expense.subCategory.lowercase(Locale.getDefault()).contains(query) ||
                            expense.category.name.lowercase(Locale.getDefault()).contains(query)
                }
            }
            .sortedByDescending { expense ->
                parseHistoryExpenseDateToMillis(expense.date) ?: Long.MIN_VALUE
            }
            .toList()

        _state.update {
            it.copy(
                expenses = filtered,
                totalExpenses = filtered.sumOf(Expense::amount),
                isLoading = isLoading,
                error = null
            )
        }
    }
}
