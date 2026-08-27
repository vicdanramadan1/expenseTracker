package com.betaapps.expensetracker.presentation.feature.home

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private const val EXPENSE_DATE_PATTERN = "d MMM yyyy"
private const val MONTH_LABEL_PATTERN = "MMM yyyy"

fun formatMonthLabel(monthMillis: Long): String {
    return SimpleDateFormat(MONTH_LABEL_PATTERN, Locale.getDefault()).format(Date(monthMillis))
}

fun parseExpenseDateToMillis(rawDate: String): Long? {
    if (rawDate.isBlank()) return null
    return runCatching {
        SimpleDateFormat(EXPENSE_DATE_PATTERN, Locale.getDefault()).parse(rawDate)?.time
    }.getOrNull()
}

fun getMonthStartMillis(dateMillis: Long): Long {
    val calendar = Calendar.getInstance().apply { timeInMillis = dateMillis }
    return getMonthStartMillis(
        year = calendar.get(Calendar.YEAR),
        month = calendar.get(Calendar.MONTH)
    )
}

fun getMonthStartMillis(year: Int, month: Int): Long {
    return Calendar.getInstance().apply {
        clear()
        set(Calendar.YEAR, year)
        set(Calendar.MONTH, month)
        set(Calendar.DAY_OF_MONTH, 1)
    }.timeInMillis
}

fun isSameMonth(firstMillis: Long, secondMillis: Long): Boolean {
    val firstCalendar = Calendar.getInstance().apply { timeInMillis = firstMillis }
    val secondCalendar = Calendar.getInstance().apply { timeInMillis = secondMillis }

    return firstCalendar.get(Calendar.YEAR) == secondCalendar.get(Calendar.YEAR) &&
            firstCalendar.get(Calendar.MONTH) == secondCalendar.get(Calendar.MONTH)
}
