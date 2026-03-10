package com.betaapps.expensetracker.presentation.home.mapper

import com.betaapps.expensetracker.domain.model.Expense as DomainExpense
import com.betaapps.expensetracker.presentation.home.model.Expense as UiExpense
import com.betaapps.expensetracker.presentation.home.model.ExpenseCategory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val DEFAULT_DATE_PATTERN = "d MMM yyyy"

fun DomainExpense.toUi(): UiExpense = UiExpense(
    id = id.toString(),
    category = ExpenseCategory.fromString(category),
    subCategory = description.orEmpty(),
    amount = amount,
    date = formatDate(createdAt)
)

fun List<DomainExpense>.toUiList(): List<UiExpense> = map { it.toUi() }

fun UiExpense.toDomain(): DomainExpense = DomainExpense(
    id = id.toLongOrNull() ?: 0L,
    amount = amount,
    category = category.name,
    description = subCategory.ifBlank { null },
    createdAt = parseDateToEpoch(date)
)

fun List<UiExpense>.toDomainList(): List<DomainExpense> = map { it.toDomain() }

private fun formatDate(epochMillis: Long, pattern: String = DEFAULT_DATE_PATTERN): String {
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    return formatter.format(Date(epochMillis))
}

private fun parseDateToEpoch(raw: String, pattern: String = DEFAULT_DATE_PATTERN): Long {
    if (raw.isBlank()) return System.currentTimeMillis()
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    return formatter.parse(raw)?.time ?: System.currentTimeMillis()
}
