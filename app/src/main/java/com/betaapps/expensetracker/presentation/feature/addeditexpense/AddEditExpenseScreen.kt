package com.betaapps.expensetracker.presentation.feature.addeditexpense

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.betaapps.expensetracker.R
import com.betaapps.expensetracker.presentation.common.ExpenseTopAppBar
import com.betaapps.expensetracker.presentation.common.ExpenseTopAppBarIconButton
import com.betaapps.expensetracker.presentation.feature.home.model.ExpenseCategory
import com.betaapps.expensetracker.ui.theme.BorderSubtle
import com.betaapps.expensetracker.ui.theme.BudgetSafe
import com.betaapps.expensetracker.ui.theme.ExpenseTrackerTheme
import com.betaapps.expensetracker.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val NOTE_MAX_LENGTH = 100

private enum class PaymentMethod(@StringRes val labelRes: Int) {
    CASH(R.string.add_edit_payment_cash),
    CARD(R.string.add_edit_payment_card)
}

@Composable
private fun formFieldTextStyle() = MaterialTheme.typography.titleMedium.copy(
    fontSize = 16.sp,
    lineHeight = 20.sp,
    fontWeight = FontWeight.SemiBold
)

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
    onSaveAsPlannedClick: () -> Unit,
    onSaveAsPaidClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isEditMode = state.expenseId != null
    val dateText = state.selectedDateMillis?.let(::formatDate).orEmpty()
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
        containerColor = Color.White,
        bottomBar = {
            SaveActionsBar(
                isEditMode = isEditMode,
                isSaving = state.isSaving,
                enabled = isFormValid,
                onSaveClick = onSaveClick,
                onSaveAsPlannedClick = onSaveAsPlannedClick,
                onSaveAsPaidClick = onSaveAsPaidClick
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(22.dp)
        ) {
            AddExpenseTopBar(
                title = stringResource(
                    if (isEditMode) R.string.add_edit_title_edit else R.string.add_edit_title_add
                ),
                canSave = isFormValid,
                onBackClick = onBackClick,
                onSaveClick = onSaveClick
            )

            AmountSection(
                value = state.amountInput,
                onValueChange = onAmountChange
            )

            CategorySection(
                selectedCategory = state.selectedCategory,
                expanded = state.isCategoryExpanded,
                onExpandedChange = onCategoryExpandedChange,
                onCategorySelected = onCategorySelected
            )

            DateSection(
                dateText = dateText,
                onClick = onDateFieldClick
            )

            PaymentMethodSection()

            NoteSection(
                value = state.subCategory,
                onValueChange = { onSubCategoryChange(it.take(NOTE_MAX_LENGTH)) }
            )

            ReceiptAttachmentBox()
        }
    }
}

@Composable
private fun AddExpenseTopBar(
    title: String,
    canSave: Boolean,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    ExpenseTopAppBar(
        title = title,
        height = 66.dp,
        horizontalPadding = 0.dp,
        titleStyle = MaterialTheme.typography.titleLarge.copy(
            fontSize = 20.sp,
            lineHeight = 24.sp,
            fontWeight = FontWeight.Bold
        ),
        navigationIcon = {
            ExpenseTopAppBarIconButton(
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(R.string.action_close),
                onClick = onBackClick,
                iconSize = 34.dp,
                buttonSize = 48.dp
            )
        },
        endContent = {
            ExpenseTopAppBarIconButton(
                imageVector = Icons.Default.Check,
                contentDescription = stringResource(R.string.add_edit_save_expense_title),
                onClick = onSaveClick,
                enabled = canSave,
                tint = if (canSave) BudgetSafe else TextSecondary.copy(alpha = 0.45f),
                iconSize = 34.dp,
                buttonSize = 48.dp
            )
        }
    )
}

@Composable
private fun AmountSection(
    value: String,
    onValueChange: (String) -> Unit
) {
    val fieldTextStyle = formFieldTextStyle()

    FormSection(label = stringResource(R.string.add_edit_amount_label)) {
        FormRow(minHeight = 70.dp) {
            IconBubble {
                Icon(
                    imageVector = Icons.Default.AttachMoney,
                    contentDescription = null,
                    tint = BudgetSafe,
                    modifier = Modifier.size(30.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                textStyle = fieldTextStyle.copy(color = MaterialTheme.colorScheme.onSurface),
                cursorBrush = SolidColor(BudgetSafe),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.weight(1f),
                decorationBox = { innerTextField ->
                    if (value.isBlank()) {
                        Text(
                            text = stringResource(R.string.add_edit_amount_placeholder),
                            color = TextSecondary.copy(alpha = 0.55f),
                            style = fieldTextStyle
                        )
                    }
                    innerTextField()
                }
            )

            if (value.isNotBlank()) {
                IconButton(
                    onClick = { onValueChange("") },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Cancel,
                        contentDescription = stringResource(R.string.add_edit_amount_clear_content_description),
                        tint = TextSecondary.copy(alpha = 0.65f),
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategorySection(
    selectedCategory: ExpenseCategory,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onCategorySelected: (ExpenseCategory) -> Unit
) {
    val fieldTextStyle = formFieldTextStyle()

    FormSection(label = stringResource(R.string.add_edit_category_label)) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = onExpandedChange
        ) {
            FormRow(
                modifier = Modifier
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled = true),
                minHeight = 64.dp
            ) {
                IconBubble {
                    Icon(
                        painter = painterResource(selectedCategory.iconRes),
                        contentDescription = stringResource(selectedCategory.labelRes),
                        tint = BudgetSafe,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = categoryLabel(selectedCategory),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = fieldTextStyle,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = TextSecondary,
                    modifier = Modifier.size(26.dp)
                )
            }

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { onExpandedChange(false) }
            ) {
                ExpenseCategory.entries.forEach { category ->
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconBubble(size = 34.dp) {
                                    Icon(
                                        painter = painterResource(category.iconRes),
                                        contentDescription = null,
                                        tint = BudgetSafe,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = categoryLabel(category),
                                    style = fieldTextStyle
                                )
                            }
                        },
                        onClick = { onCategorySelected(category) },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }
        }
    }
}

@Composable
private fun DateSection(
    dateText: String,
    onClick: () -> Unit
) {
    val fieldTextStyle = formFieldTextStyle()

    FormSection(label = stringResource(R.string.add_edit_date_label)) {
        FormRow(
            modifier = Modifier.clickable(onClick = onClick),
            minHeight = 64.dp
        ) {
            IconBubble {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    tint = BudgetSafe,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = dateText.ifBlank { stringResource(R.string.add_edit_date_placeholder) },
                color = if (dateText.isBlank()) TextSecondary else MaterialTheme.colorScheme.onSurface,
                style = fieldTextStyle,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = stringResource(R.string.action_pick),
                tint = TextSecondary,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PaymentMethodSection() {
    var expanded by rememberSaveable { mutableStateOf(false) }
    var selectedPaymentMethod by rememberSaveable { mutableStateOf(PaymentMethod.CASH) }
    val fieldTextStyle = formFieldTextStyle()

    FormSection(label = stringResource(R.string.add_edit_payment_method_label)) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            FormRow(
                modifier = Modifier
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled = true),
                minHeight = 64.dp
            ) {
                IconBubble {
                    Icon(
                        imageVector = Icons.Default.AccountBalanceWallet,
                        contentDescription = null,
                        tint = BudgetSafe,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = stringResource(selectedPaymentMethod.labelRes),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = fieldTextStyle,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = TextSecondary,
                    modifier = Modifier.size(26.dp)
                )
            }

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                PaymentMethod.entries.forEach { paymentMethod ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = stringResource(paymentMethod.labelRes),
                                style = fieldTextStyle
                            )
                        },
                        onClick = {
                            selectedPaymentMethod = paymentMethod
                            expanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }
        }
    }
}

@Composable
private fun NoteSection(
    value: String,
    onValueChange: (String) -> Unit
) {
    val fieldTextStyle = formFieldTextStyle()

    FormSection(label = stringResource(R.string.add_edit_note_optional_label)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(112.dp)
                .background(Color.White, RoundedCornerShape(18.dp))
                .border(1.dp, BorderSubtle, RoundedCornerShape(18.dp))
                .padding(horizontal = 14.dp, vertical = 14.dp)
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = fieldTextStyle.copy(color = MaterialTheme.colorScheme.onSurface),
                cursorBrush = SolidColor(BudgetSafe),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 58.dp),
                decorationBox = { innerTextField ->
                    if (value.isBlank()) {
                        Text(
                            text = stringResource(R.string.add_edit_note_placeholder),
                            color = TextSecondary.copy(alpha = 0.60f),
                            style = fieldTextStyle
                        )
                    }
                    innerTextField()
                }
            )

            Text(
                text = stringResource(R.string.add_edit_note_count, value.length, NOTE_MAX_LENGTH),
                color = TextSecondary.copy(alpha = 0.75f),
                style = MaterialTheme.typography.bodySmall,
                fontSize = 13.sp,
                lineHeight = 16.sp,
                modifier = Modifier.align(Alignment.BottomEnd)
            )
        }
    }
}

@Composable
private fun ReceiptAttachmentBox() {
    val shape = RoundedCornerShape(18.dp)
    val strokeColor = BorderSubtle
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(126.dp)
            .drawBehind {
                drawRoundRect(
                    color = strokeColor,
                    cornerRadius = CornerRadius(18.dp.toPx(), 18.dp.toPx()),
                    style = Stroke(
                        width = 1.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(
                            floatArrayOf(10.dp.toPx(), 7.dp.toPx())
                        )
                    )
                )
            }
            .background(Color.White, shape)
            .padding(18.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = Icons.Default.AttachFile,
                contentDescription = null,
                tint = BudgetSafe,
                modifier = Modifier.size(32.dp)
            )
            Text(
                text = stringResource(R.string.add_edit_attach_receipt),
                color = TextSecondary,
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 15.sp,
                lineHeight = 19.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = stringResource(R.string.add_edit_receipt_hint),
                color = TextSecondary.copy(alpha = 0.72f),
                style = MaterialTheme.typography.bodySmall,
                fontSize = 13.sp,
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
private fun SaveActionsBar(
    isEditMode: Boolean,
    isSaving: Boolean,
    enabled: Boolean,
    onSaveClick: () -> Unit,
    onSaveAsPlannedClick: () -> Unit,
    onSaveAsPaidClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .navigationBarsPadding()
            .imePadding()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        SaveActions(
            isEditMode = isEditMode,
            isSaving = isSaving,
            enabled = enabled,
            onSaveClick = onSaveClick,
            onSaveAsPlannedClick = onSaveAsPlannedClick,
            onSaveAsPaidClick = onSaveAsPaidClick
        )
    }
}

@Composable
private fun SaveActions(
    isEditMode: Boolean,
    isSaving: Boolean,
    enabled: Boolean,
    onSaveClick: () -> Unit,
    onSaveAsPlannedClick: () -> Unit,
    onSaveAsPaidClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SaveActionCard(
            title = stringResource(R.string.add_edit_save_planned_title),
            subtitle = stringResource(R.string.add_edit_save_planned_subtitle),
            filled = false,
            enabled = enabled,
            onClick = onSaveAsPlannedClick,
            modifier = Modifier.weight(1f)
        )

        SaveActionCard(
            title = when {
                isSaving -> stringResource(R.string.add_edit_button_saving)
                isEditMode -> stringResource(R.string.add_edit_button_save_changes)
                else -> stringResource(R.string.add_edit_save_expense_title)
            },
            subtitle = stringResource(R.string.add_edit_save_expense_subtitle),
            filled = true,
            enabled = enabled,
            onClick = if (isEditMode) onSaveClick else onSaveAsPaidClick,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun SaveActionCard(
    title: String,
    subtitle: String,
    filled: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(16.dp)
    val containerColor = if (filled) BudgetSafe else Color.White
    val contentColor = if (filled) Color.White else MaterialTheme.colorScheme.onSurface
    val subtitleColor = if (filled) Color.White.copy(alpha = 0.84f) else TextSecondary
    val alpha = if (enabled) 1f else 0.55f

    Row(
        modifier = modifier
            .height(80.dp)
            .background(containerColor.copy(alpha = if (filled) alpha else 1f), shape)
            .border(
                border = BorderStroke(
                    width = 1.dp,
                    color = if (filled) Color.Transparent else BorderSubtle
                ),
                shape = shape
            )
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.Article,
            contentDescription = null,
            tint = if (filled) Color.White else BudgetSafe,
            modifier = Modifier.size(34.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = title,
                color = contentColor.copy(alpha = alpha),
                style = MaterialTheme.typography.titleMedium,
                fontSize = 15.sp,
                lineHeight = 19.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = subtitle,
                color = subtitleColor.copy(alpha = alpha),
                style = MaterialTheme.typography.bodySmall,
                fontSize = 13.sp,
                lineHeight = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun FormSection(
    label: String,
    content: @Composable () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = label,
            color = Color(0xFF454B63),
            style = MaterialTheme.typography.labelLarge,
            fontSize = 14.sp,
            lineHeight = 18.sp,
            fontWeight = FontWeight.Medium
        )
        content()
    }
}

@Composable
private fun FormRow(
    modifier: Modifier = Modifier,
    minHeight: androidx.compose.ui.unit.Dp = 64.dp,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = minHeight)
            .defaultMinSize(minHeight = minHeight)
            .background(Color.White, RoundedCornerShape(18.dp))
            .border(1.dp, BorderSubtle, RoundedCornerShape(18.dp))
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        content()
    }
}

@Composable
private fun IconBubble(
    modifier: Modifier = Modifier,
    size: androidx.compose.ui.unit.Dp = 42.dp,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .size(size)
            .background(BudgetSafe.copy(alpha = 0.10f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
private fun categoryLabel(category: ExpenseCategory): String {
    return when (category) {
        ExpenseCategory.FOOD -> stringResource(R.string.category_food_dining)
        else -> stringResource(category.labelRes)
    }
}

@Preview(showBackground = true)
@Composable
private fun AddExpenseScreenPreview() {
    ExpenseTrackerTheme {
        AddEditExpenseScreen(
            state = AddEditExpenseState(
                selectedCategory = ExpenseCategory.FOOD,
                subCategory = "Lunch at Cafe",
                amountInput = "12.50",
                selectedDateMillis = 1781222400000L
            ),
            onAmountChange = {},
            onCategoryExpandedChange = {},
            onCategorySelected = {},
            onSubCategoryChange = {},
            onDateFieldClick = {},
            onDatePickerDismiss = {},
            onDateSelected = {},
            onSaveClick = {},
            onSaveAsPlannedClick = {},
            onSaveAsPaidClick = {},
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
            onSaveAsPlannedClick = {},
            onSaveAsPaidClick = {},
            onBackClick = {}
        )
    }
}

private fun formatDate(dateMillis: Long): String {
    val formatter = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
    return formatter.format(Date(dateMillis))
}
