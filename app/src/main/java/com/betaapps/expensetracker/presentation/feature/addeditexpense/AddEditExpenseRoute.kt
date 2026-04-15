package com.betaapps.expensetracker.presentation.feature.addeditexpense

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun AddEditExpenseRoute(
    expenseId: String?,
    onBackClick: () -> Unit,
    onSaveDone: () -> Unit
) {
    val viewModel: AddEditExpenseViewModel = hiltViewModel()
    val state = viewModel.state.collectAsState().value

    LaunchedEffect(expenseId) {
        viewModel.loadExpense(expenseId)
    }

    AddEditExpenseScreen(
        state = state,
        onAmountChange = viewModel::onAmountChange,
        onCategoryExpandedChange = viewModel::onCategoryExpandedChange,
        onCategorySelected = viewModel::onCategorySelected,
        onSubCategoryChange = viewModel::onSubCategoryChange,
        onDateChange = viewModel::onDateChange,
        onSaveClick = {
            viewModel.saveExpense()
            onSaveDone()
        },
        onBackClick = onBackClick,
    )
}
