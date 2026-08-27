package com.betaapps.expensetracker.presentation.feature.history

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.betaapps.expensetracker.presentation.feature.home.model.Expense

@Composable
fun HistoryRoute(
    onExpenseClick: (Expense) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState().value

    HistoryScreen(
        state = state,
        onSearchQueryChange = viewModel::onSearchQueryChange,
        onStatusFilterSelected = viewModel::onStatusFilterSelected,
        onMonthFieldClick = viewModel::onMonthFieldClick,
        onExpenseClick = onExpenseClick,
        modifier = modifier
    )
}
