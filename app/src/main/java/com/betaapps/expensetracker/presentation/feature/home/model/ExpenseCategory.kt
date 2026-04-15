package com.betaapps.expensetracker.presentation.feature.home.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.betaapps.expensetracker.R
import com.betaapps.expensetracker.ui.theme.CategoryBills
import com.betaapps.expensetracker.ui.theme.CategoryEducation
import com.betaapps.expensetracker.ui.theme.CategoryEntertainment
import com.betaapps.expensetracker.ui.theme.CategoryFood
import com.betaapps.expensetracker.ui.theme.CategoryHealth
import com.betaapps.expensetracker.ui.theme.CategoryHousing
import com.betaapps.expensetracker.ui.theme.CategoryOther
import com.betaapps.expensetracker.ui.theme.CategoryShopping
import com.betaapps.expensetracker.ui.theme.CategoryTransport
import com.betaapps.expensetracker.ui.theme.CategoryTravel
import java.util.Locale

/**
 * Central category catalog used by charts and UI to keep colors and icons consistent.
 */
enum class ExpenseCategory(
    val label: String,
    val color: Color,
    @DrawableRes val iconRes: Int
) {
    FOOD("Food", CategoryFood, R.drawable.food),
    TRANSPORT("Transport", CategoryTransport, android.R.drawable.ic_menu_directions),
    ENTERTAINMENT("Entertainment", CategoryEntertainment, android.R.drawable.ic_media_play),
    HOUSING("Housing", CategoryHousing, android.R.drawable.ic_menu_myplaces),
    BILLS("Bills", CategoryBills, android.R.drawable.ic_menu_agenda),
    HEALTH("Health", CategoryHealth, android.R.drawable.ic_menu_info_details),
    SHOPPING("Shopping", CategoryShopping, android.R.drawable.ic_menu_crop),
    EDUCATION("Education", CategoryEducation, android.R.drawable.ic_menu_edit),
    TRAVEL("Travel", CategoryTravel, android.R.drawable.ic_menu_compass),
    OTHER("Other", CategoryOther, android.R.drawable.ic_menu_help);

    companion object {
        fun fromString(raw: String?): ExpenseCategory {
            val normalized = raw?.trim().orEmpty()
            if (normalized.isEmpty()) return OTHER

            val normalizedLower = normalized.lowercase(Locale.US)
            return entries.firstOrNull { entry ->
                entry.name.lowercase(Locale.US) == normalizedLower ||
                    entry.label.lowercase(Locale.US) == normalizedLower
            } ?: OTHER
        }
    }
}
