package com.betaapps.expensetracker.presentation.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.betaapps.expensetracker.R
import com.betaapps.expensetracker.presentation.feature.addeditexpense.AddEditExpenseRoute
import com.betaapps.expensetracker.presentation.feature.home.HomeScreen
import com.betaapps.expensetracker.presentation.feature.search.SearchExpensesRoute
import kotlinx.serialization.Serializable

@Serializable
sealed interface ExpenseDestination : NavKey {
    @Serializable
    data object Home : ExpenseDestination

    @Serializable
    data object Search : ExpenseDestination

    @Serializable
    data class AddEdit(val expenseId: String? = null) : ExpenseDestination
}

@Composable
fun ExpenseNavHost(
    modifier: Modifier = Modifier
) {
    val backStack = rememberNavBackStack(ExpenseDestination.Home)
    val popIfPossible = { if (backStack.size > 1) backStack.removeAt(backStack.lastIndex) }

    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        onBack = {
            if (backStack.size > 1) {
               popIfPossible()
            }
        },
        entryProvider = entryProvider(
            fallback = { key ->
                NavEntry(key) { Text(text = stringResource(R.string.unknown_destination)) }
            }
        ) {
            entry<ExpenseDestination.Home> {
                HomeScreen(
                    onAddExpenseClick = { backStack.add(ExpenseDestination.AddEdit()) },
                    onSearchClick = { backStack.add(ExpenseDestination.Search) },
                    onExpenseClick = { expense ->
                        backStack.add(ExpenseDestination.AddEdit(expenseId = expense.id))
                    }
                )
            }
            entry<ExpenseDestination.Search> {
                SearchExpensesRoute(
                    onBackClick = { popIfPossible() },
                    onExpenseClick = { expense ->
                        backStack.add(ExpenseDestination.AddEdit(expenseId = expense.id))
                    }
                )
            }
            entry<ExpenseDestination.AddEdit> { destination ->
                AddEditExpenseRoute(
                    expenseId = destination.expenseId,
                    onSaveDone = { backStack.removeAt(backStack.lastIndex) },
                    onBackClick = {  popIfPossible() }
                )
            }
        }
    )
}
