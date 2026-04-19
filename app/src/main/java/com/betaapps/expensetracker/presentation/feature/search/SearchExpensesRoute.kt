package com.betaapps.expensetracker.presentation.feature.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.betaapps.expensetracker.presentation.feature.home.HomeScreenViewModel
import com.betaapps.expensetracker.presentation.feature.home.model.Expense

@Composable
fun SearchExpensesRoute(
    onBackClick: () -> Unit,
    onExpenseClick: (Expense) -> Unit
) {
    val viewModel: HomeScreenViewModel = hiltViewModel()
    val state = viewModel.homeState.collectAsState().value

    SearchExpensesScreen(
        state = state,
        onSearchQueryChange = viewModel::onSearchQueryChange,
        onExpenseClick = onExpenseClick,
        onExpenseSwipedToDelete = viewModel::removeExpense,
        onBackClick = onBackClick
    )
}
