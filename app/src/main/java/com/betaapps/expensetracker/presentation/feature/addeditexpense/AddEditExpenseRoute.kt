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
    LaunchedEffect(viewModel) {
        viewModel.uiEvents.collect { event ->
            when (event) {
                AddEditExpenseUiEvent.SaveSuccess -> onSaveDone()
            }
        }
    }

    AddEditExpenseScreen(
        state = state,
        onAmountChange = viewModel::onAmountChange,
        onCategoryExpandedChange = viewModel::onCategoryExpandedChange,
        onCategorySelected = viewModel::onCategorySelected,
        onSubCategoryChange = viewModel::onSubCategoryChange,
        onDateFieldClick = viewModel::onDateFieldClick,
        onDatePickerDismiss = viewModel::onDatePickerDismiss,
        onDateSelected = viewModel::onDateSelected,
        onSaveClick = { viewModel.saveExpense() },
        onSaveAsPlannedClick = { viewModel.saveExpense(isPaid = false) },
        onSaveAsPaidClick = { viewModel.saveExpense(isPaid = true) },
        onBackClick = onBackClick,
    )
}
