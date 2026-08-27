package com.betaapps.expensetracker.presentation.feature.history

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private const val EXPENSE_DATE_PATTERN = "d MMM yyyy"
private const val MONTH_LABEL_PATTERN = "MMM yyyy"
private const val HISTORY_DATE_PATTERN = "MMM d, yyyy"

fun formatHistoryMonthLabel(monthMillis: Long): String {
    return SimpleDateFormat(MONTH_LABEL_PATTERN, Locale.getDefault()).format(Date(monthMillis))
}

fun formatHistoryDateLabel(dateMillis: Long): String {
    return SimpleDateFormat(HISTORY_DATE_PATTERN, Locale.getDefault()).format(Date(dateMillis))
}

fun parseHistoryExpenseDateToMillis(rawDate: String): Long? {
    if (rawDate.isBlank()) return null
    return runCatching {
        SimpleDateFormat(EXPENSE_DATE_PATTERN, Locale.getDefault()).parse(rawDate)?.time
    }.getOrNull()
}

fun getHistoryMonthStartMillis(dateMillis: Long): Long {
    val calendar = Calendar.getInstance().apply { timeInMillis = dateMillis }
    return getHistoryMonthStartMillis(
        year = calendar.get(Calendar.YEAR),
        month = calendar.get(Calendar.MONTH)
    )
}

fun getHistoryMonthStartMillis(year: Int, month: Int): Long {
    return Calendar.getInstance().apply {
        clear()
        set(Calendar.YEAR, year)
        set(Calendar.MONTH, month)
        set(Calendar.DAY_OF_MONTH, 1)
    }.timeInMillis
}

fun getHistoryDayStartMillis(dateMillis: Long): Long {
    return Calendar.getInstance().apply {
        timeInMillis = dateMillis
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis
}

fun isHistorySameMonth(firstMillis: Long, secondMillis: Long): Boolean {
    val firstCalendar = Calendar.getInstance().apply { timeInMillis = firstMillis }
    val secondCalendar = Calendar.getInstance().apply { timeInMillis = secondMillis }

    return firstCalendar.get(Calendar.YEAR) == secondCalendar.get(Calendar.YEAR) &&
            firstCalendar.get(Calendar.MONTH) == secondCalendar.get(Calendar.MONTH)
}

fun isHistorySameDay(firstMillis: Long, secondMillis: Long): Boolean {
    val firstCalendar = Calendar.getInstance().apply { timeInMillis = firstMillis }
    val secondCalendar = Calendar.getInstance().apply { timeInMillis = secondMillis }

    return firstCalendar.get(Calendar.YEAR) == secondCalendar.get(Calendar.YEAR) &&
            firstCalendar.get(Calendar.DAY_OF_YEAR) == secondCalendar.get(Calendar.DAY_OF_YEAR)
}

fun isHistoryYesterday(dateMillis: Long, todayMillis: Long): Boolean {
    val yesterdayMillis = Calendar.getInstance().apply {
        timeInMillis = todayMillis
        add(Calendar.DAY_OF_YEAR, -1)
    }.timeInMillis
    return isHistorySameDay(dateMillis, yesterdayMillis)
}
