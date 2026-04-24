package com.betaapps.expensetracker.ui.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.betaapps.expensetracker.presentation.model.BudgetStatus

private val DarkColorScheme = darkColorScheme(
    primary = AccentPrimary,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    background = Color(0xFF0B1220),
    surface = Color(0xFF0F172A),
    onPrimary = Color.White,
    onBackground = Color(0xFFE5E7EB),
    onSurface = Color(0xFFE5E7EB),
    onSurfaceVariant = Color(0xFF94A3B8),
    outlineVariant = Color(0xFF334155),
    scrim = ShadowSoft
)

private val LightColorScheme = lightColorScheme(
    primary = AccentPrimary,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    background = ScreenBackground,
    surface = Color.White,
    onPrimary = Color.White,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    onSurfaceVariant = TextSecondary,
    outlineVariant = BorderSubtle,
    scrim = ShadowSoft
)

@Composable
fun ExpenseTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    budgetStatus: BudgetStatus = BudgetStatus.SAFE,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val primaryColor = when(budgetStatus)
    {
         BudgetStatus.SAFE -> { BudgetSafe }
         BudgetStatus.DANGER -> { BudgetWarning }
        BudgetStatus.WARNING -> { BudgetDanger }
    }

    val animatedPrimaryColor by animateColorAsState(
        targetValue = primaryColor,
        label = "PrimaryColorAnimation"
    )
    
    val colorScheme = remember(animatedPrimaryColor) {
        lightColorScheme(
            primary = animatedPrimaryColor,
            secondary = animatedPrimaryColor,
            background = Color(0xFFF8FAFC),
            surface = Color.White,
            onPrimary = Color.White,
            onSurface = Color(0xFF0F172A)
        )
    }


    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
