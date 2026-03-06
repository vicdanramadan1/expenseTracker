package com.betaapps.expensetracker.domain.usecases

import com.betaapps.expensetracker.domain.model.Expense
import com.betaapps.expensetracker.domain.repository.ExpenseRepository

class AddExpensUsecase(val repository: ExpenseRepository){
    suspend operator fun invoke(expense: Expense)
    {
        repository.addExpense(expense)
    }
}