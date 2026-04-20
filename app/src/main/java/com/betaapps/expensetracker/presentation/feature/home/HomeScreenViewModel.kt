package com.betaapps.expensetracker.presentation.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.betaapps.expensetracker.domain.usecases.AddExpenseUsecase
import com.betaapps.expensetracker.domain.usecases.DeleteExpenseUsecase
import com.betaapps.expensetracker.domain.usecases.GetExpensesUsecase
import com.betaapps.expensetracker.domain.usecases.SearchExpensesUsecase
import com.betaapps.expensetracker.presentation.feature.home.mapper.toDomain
import com.betaapps.expensetracker.presentation.feature.home.mapper.toUiList
import com.betaapps.expensetracker.presentation.feature.home.model.Expense
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getExpensesUsecase: GetExpensesUsecase,
    private val addExpenseUsecase: AddExpenseUsecase,
    private val deleteExpenseUsecase: DeleteExpenseUsecase,
    private val searchExpensesUsecase: SearchExpensesUsecase,
) : ViewModel() {

    private val _homeState = MutableStateFlow(HomeState())
    val homeState: StateFlow<HomeState> = _homeState.asStateFlow()
    private var loadJob: Job? = null
    init {
        loadExpenses()
    }

    fun onEvent(event: HomeScreenEvent) {
        when (event) {
            HomeScreenEvent.LoadExpenses -> loadExpenses()
            is HomeScreenEvent.ExpenseTileClicked -> {}
            is HomeScreenEvent.AddNewExpenseClicked -> {
                 addExpense(event.expense)

            }
        }
    }

    private fun setExpenses(expenses: List<Expense>) {
        _homeState.update { current ->
            current.copy(isLoading = false, error = null, data = expenses)
        }
    }

    private fun upsertExpense(expense: Expense) {
        val currentList = _homeState.value.data.orEmpty()
        val updated = if (currentList.any { it.id == expense.id }) {
            currentList.map { if (it.id == expense.id) expense else it }
        } else {
            listOf(expense) + currentList
        }

        _homeState.update { current ->
            current.copy(isLoading = false, error = "", data = updated)
        }
    }

    fun removeExpense(expense: Expense) {
        viewModelScope.launch {
            deleteExpenseUsecase(expense.toDomain())
        }
    }

    fun onSearchQueryChange(query: String) {
        val normalizedQuery = query.trim()
        _homeState.update { it.copy(searchQuery = query) }

        if (normalizedQuery.isBlank()) {
            loadExpenses()
            return
        }

        loadJob?.cancel()
        _homeState.update { it.copy(isLoading = true, error = null, data = emptyList()) }
        loadJob = viewModelScope.launch {
            searchExpensesUsecase(normalizedQuery).collect { expenses ->
                _homeState.update {
                    it.copy(isLoading = false, error = null, data = expenses.toUiList())
                }
            }
        }
    }

    private fun loadExpenses() {
        loadJob?.cancel()
        _homeState.update { it.copy(isLoading = true, error = null) }
        loadJob = viewModelScope.launch {
            getExpensesUsecase().collect { expenses ->
                _homeState.update { it.copy(isLoading = false, error = null, data = expenses.toUiList()) }
            }
        }
    }

    fun addExpense(expense: Expense) {
        viewModelScope.launch {
            addExpenseUsecase(expense.toDomain())
        }
    }
}
