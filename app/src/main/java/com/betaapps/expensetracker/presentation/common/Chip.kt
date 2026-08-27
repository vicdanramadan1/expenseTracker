package com.betaapps.expensetracker.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.betaapps.expensetracker.ui.theme.BorderSubtle
import com.betaapps.expensetracker.ui.theme.BudgetSafe

@Composable
fun Chip(
    label: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    horizontalPadding: Dp = 16.dp
) {
    val containerColor = if (selected) BudgetSafe else Color.White
    val contentColor = if (selected) Color.White else MaterialTheme.colorScheme.onSurface
    val borderColor = if (selected) BudgetSafe else BorderSubtle
    val clickableModifier = if (onClick != null) {
        Modifier.clickable(onClick = onClick)
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .height(36.dp)
            .clip(RoundedCornerShape(999.dp))
            .background(containerColor)
            .border(1.dp, borderColor, RoundedCornerShape(999.dp))
            .then(clickableModifier)
            .padding(horizontal = horizontalPadding),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = contentColor,
            style = MaterialTheme.typography.titleMedium,
            fontSize = 13.sp,
            lineHeight = 16.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1
        )
    }
}