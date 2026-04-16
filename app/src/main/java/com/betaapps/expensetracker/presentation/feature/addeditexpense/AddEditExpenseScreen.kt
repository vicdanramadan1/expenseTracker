package com.betaapps.expensetracker.presentation.feature.addeditexpense

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.betaapps.expensetracker.R
import com.betaapps.expensetracker.presentation.feature.home.model.ExpenseCategory
import com.betaapps.expensetracker.ui.theme.ExpenseTrackerTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditExpenseScreen(
    state: AddEditExpenseState,
    onAmountChange: (String) -> Unit,
    onCategoryExpandedChange: (Boolean) -> Unit,
    onCategorySelected: (ExpenseCategory) -> Unit,
    onSubCategoryChange: (String) -> Unit,
    onDateFieldClick: () -> Unit,
    onDatePickerDismiss: () -> Unit,
    onDateSelected: (Long) -> Unit,
    onSaveClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isEditMode = state.expenseId != null
    val dateText = state.selectedDateMillis?.let(::formatDate).orEmpty()
    val cardShape = RoundedCornerShape(18.dp)
    val cardBorder = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    val isFormValid by remember(state) {
        derivedStateOf {
          state.amountInput.isNotBlank() &&
                  !state.isSaving &&
                  state.selectedDateMillis != null
        }
    }

    if (state.isDatePickerVisible) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = state.selectedDateMillis ?: System.currentTimeMillis()
        )
        DatePickerDialog(
            onDismissRequest = onDatePickerDismiss,
            confirmButton = {
                TextButton(
                    onClick = {
                        val selectedDateMillis = datePickerState.selectedDateMillis
                        if (selectedDateMillis != null) {
                            onDateSelected(selectedDateMillis)
                        } else {
                            onDatePickerDismiss()
                        }
                    }
                ) {
                    Text(stringResource(R.string.action_ok))
                }
            },
            dismissButton = {
                TextButton(onClick = onDatePickerDismiss) {
                    Text(stringResource(R.string.action_cancel))
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = stringResource(
                            if (isEditMode) R.string.add_edit_title_edit else R.string.add_edit_title_add
                        ),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = stringResource(
                            if (isEditMode) R.string.add_edit_subtitle_edit else R.string.add_edit_subtitle_add
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                TextButton(onClick = onBackClick) {
                    Text(stringResource(R.string.action_close))
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 10.dp,
                        shape = cardShape,
                        ambientColor = MaterialTheme.colorScheme.scrim,
                        spotColor = MaterialTheme.colorScheme.scrim
                    ),
                shape = cardShape,
                border = cardBorder,
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = state.amountInput,
                        onValueChange = onAmountChange,
                        label = { Text(stringResource(R.string.add_edit_amount_label)) },
                        placeholder = { Text(stringResource(R.string.add_edit_amount_placeholder)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next,
                        )
                    )

                    ExposedDropdownMenuBox(
                        expanded = state.isCategoryExpanded,
                        onExpandedChange = onCategoryExpandedChange
                    ) {
                        OutlinedTextField(
                            value = stringResource(state.selectedCategory.labelRes),
                            onValueChange = {},
                            readOnly = true,
                            label = { Text(stringResource(R.string.add_edit_category_label)) },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expanded = state.isCategoryExpanded
                                )
                            },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = state.isCategoryExpanded,
                            onDismissRequest = { onCategoryExpandedChange(false) }
                        ) {
                            ExpenseCategory.entries.forEach { category ->
                                DropdownMenuItem(
                                    text = {
                                        CategoryItem(
                                            color = category.color,
                                            label = stringResource(category.labelRes)
                                        )
                                    },
                                    onClick = {
                                        onCategorySelected(category)
                                    }
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = state.subCategory,
                        onValueChange = onSubCategoryChange,
                        label = { Text(stringResource(R.string.add_edit_subcategory_optional_label)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        )
                    )

                    OutlinedTextField(
                        value = dateText,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(stringResource(R.string.add_edit_date_label)) },
                        placeholder = { Text(stringResource(R.string.add_edit_date_placeholder)) },
                        trailingIcon = {
                            TextButton(onClick = onDateFieldClick) {
                                Text(stringResource(R.string.action_pick))
                            }
                        },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = onDateFieldClick)
                    )
                }
            }

            Button(
                onClick = onSaveClick,
                enabled = isFormValid,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = when {
                        state.isSaving -> stringResource(R.string.add_edit_button_saving)
                        isEditMode -> stringResource(R.string.add_edit_button_save_changes)
                        else -> stringResource(R.string.add_edit_button_add_expense)
                    },
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun CategoryItem(
    color: Color,
    label: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(9.dp)
                .background(color, CircleShape)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AddExpenseScreenPreview() {
    ExpenseTrackerTheme {
        AddEditExpenseScreen(
            state = AddEditExpenseState(),
            onAmountChange = {},
            onCategoryExpandedChange = {},
            onCategorySelected = {},
            onSubCategoryChange = {},
            onDateFieldClick = {},
            onDatePickerDismiss = {},
            onDateSelected = {},
            onSaveClick = {},
            onBackClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EditExpenseScreenPreview() {
    ExpenseTrackerTheme {
        AddEditExpenseScreen(
            state = AddEditExpenseState(
                expenseId = "1",
                selectedCategory = ExpenseCategory.TRANSPORT,
                subCategory = "Taxi",
                amountInput = "32.0",
                selectedDateMillis = 1709856000000L
            ),
            onAmountChange = {},
            onCategoryExpandedChange = {},
            onCategorySelected = {},
            onSubCategoryChange = {},
            onDateFieldClick = {},
            onDatePickerDismiss = {},
            onDateSelected = {},
            onSaveClick = {},
            onBackClick = {}
        )
    }
}

private fun formatDate(dateMillis: Long): String {
    val formatter = SimpleDateFormat("d MMM yyyy", Locale.getDefault())
    return formatter.format(Date(dateMillis))
}
