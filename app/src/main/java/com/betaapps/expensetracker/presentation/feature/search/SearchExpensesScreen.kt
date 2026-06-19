package com.betaapps.expensetracker.presentation.feature.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.material3.CircularProgressIndicator
import com.betaapps.expensetracker.R
import com.betaapps.expensetracker.presentation.common.ExpenseTopAppBar
import com.betaapps.expensetracker.presentation.feature.home.HomeState
import com.betaapps.expensetracker.presentation.feature.home.components.ExpensesList
import com.betaapps.expensetracker.presentation.feature.home.model.Expense

@Composable
fun SearchExpensesScreen(
    state: HomeState,
    onSearchQueryChange: (String) -> Unit,
    onExpenseClick: (Expense) -> Unit,
    onExpenseSwipedToDelete: (Expense) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }
    val expenses = state.data.orEmpty()
    val normalizedQuery = state.searchQuery.trim()

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            ExpenseTopAppBar(
                title = stringResource(R.string.search_title),
                height = 52.dp,
                horizontalPadding = 0.dp,
                titleStyle = MaterialTheme.typography.headlineSmall,
                endContent = {
                    TextButton(onClick = onBackClick) {
                        Text(stringResource(R.string.action_close))
                    }
                }
            )

            Text(
                text = stringResource(R.string.search_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = onSearchQueryChange,
                label = { Text(stringResource(R.string.home_search_label)) },
                placeholder = { Text(stringResource(R.string.home_search_placeholder)) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                )
            )

            when {
                normalizedQuery.isBlank() -> {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.search_empty_state),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                state.isLoading -> {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                expenses.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.home_search_no_results),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                else -> {
                    ExpensesList(
                        expenses = expenses,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        onExpenseClicked = onExpenseClick,
                        onExpenseSwipedToDelete = onExpenseSwipedToDelete
                    )
                }
            }
        }
    }
}
