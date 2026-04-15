package com.betaapps.expensetracker.presentation.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.betaapps.expensetracker.presentation.feature.addeditexpense.AddEditExpenseRoute
import com.betaapps.expensetracker.presentation.feature.home.HomeScreen
import kotlinx.serialization.Serializable

@Serializable
sealed interface ExpenseDestination : NavKey {
    @Serializable
    data object Home : ExpenseDestination

    @Serializable
    data class AddEdit(val expenseId: String? = null) : ExpenseDestination
}

@Composable
fun ExpenseNavHost(
    modifier: Modifier = Modifier
) {
    val backStack = rememberNavBackStack(ExpenseDestination.Home)

    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        onBack = {
            if (backStack.size > 1) {
                backStack.removeAt(backStack.lastIndex)
            }
        },
        entryProvider = entryProvider(
            fallback = { key ->
                NavEntry(key) { Text(text = "Unknown destination") }
            }
        ) {
            entry<ExpenseDestination.Home> {
                HomeScreen(
                    onAddExpenseClick = { backStack.add(ExpenseDestination.AddEdit()) },
                    onExpenseClick = { expense ->
                        backStack.add(ExpenseDestination.AddEdit(expenseId = expense.id))
                    }
                )
            }
            entry<ExpenseDestination.AddEdit> { destination ->
                AddEditExpenseRoute(
                    expenseId = destination.expenseId,
                    onSaveDone = { backStack.removeAt(backStack.lastIndex) },
                    onBackClick = { backStack.removeAt(backStack.lastIndex) }
                )
            }
        }
    )
}
