package com.betaapps.expensetracker.presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.betaapps.expensetracker.R
import com.betaapps.expensetracker.ui.theme.BudgetSafe
import java.text.DateFormatSymbols
import java.util.Calendar
import java.util.Locale

@Composable
fun MonthPicker(
    selectedMonthMillis: Long,
    onMonthSelected: (year: Int, month: Int) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    selectedColor: Color = BudgetSafe
) {
    val selectedCalendar = remember(selectedMonthMillis) {
        Calendar.getInstance().apply { timeInMillis = selectedMonthMillis }
    }
    val selectedYear = selectedCalendar.get(Calendar.YEAR)
    val selectedMonth = selectedCalendar.get(Calendar.MONTH)
    var visibleYear by remember(selectedMonthMillis) { mutableIntStateOf(selectedYear) }
    val monthNames = remember { getShortMonthNames() }

    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(R.string.budget_month_picker_title))
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = { visibleYear -= 1 }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = stringResource(R.string.budget_previous_year_content_description)
                        )
                    }
                    Text(
                        text = visibleYear.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    TextButton(onClick = { visibleYear += 1 }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = stringResource(R.string.budget_next_year_content_description)
                        )
                    }
                }

                monthNames.chunked(3).forEachIndexed { rowIndex, rowMonths ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        rowMonths.forEachIndexed { columnIndex, monthName ->
                            val monthIndex = rowIndex * 3 + columnIndex
                            val isSelected = visibleYear == selectedYear && monthIndex == selectedMonth
                            TextButton(
                                onClick = { onMonthSelected(visibleYear, monthIndex) },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 1.dp),
                                colors = ButtonDefaults.textButtonColors(
                                    containerColor = if (isSelected) {
                                        selectedColor.copy(alpha = 0.12f)
                                    } else {
                                        Color.Transparent
                                    },
                                    contentColor = if (isSelected) {
                                        selectedColor
                                    } else {
                                        MaterialTheme.colorScheme.onSurface
                                    }
                                )
                            ) {
                                Text(
                                    text = monthName,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.action_cancel))
            }
        }
    )
}

private fun getShortMonthNames(): List<String> {
    return DateFormatSymbols.getInstance(Locale.getDefault())
        .shortMonths
        .filter { it.isNotBlank() }
        .take(12)
}
