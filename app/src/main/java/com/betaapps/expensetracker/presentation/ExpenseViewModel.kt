package com.betaapps.expensetracker.presentation

import androidx.lifecycle.ViewModel
import com.betaapps.expensetracker.domain.usecases.AddExpenseUsecase
import com.betaapps.expensetracker.domain.usecases.DeleteExpenseUsecase
import com.betaapps.expensetracker.domain.usecases.GetExpensesUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val addExpenseUsecase: AddExpenseUsecase,
    private val deleteExpensUsecase: DeleteExpenseUsecase,
    private val getExpensesUsecase: GetExpensesUsecase
) : ViewModel(){

}
