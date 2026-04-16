package com.betaapps.expensetracker.presentation.feature.home.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
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
    @StringRes val labelRes: Int,
    val color: Color,
    @DrawableRes val iconRes: Int
) {
    FOOD(R.string.category_food, CategoryFood, R.drawable.food),
    TRANSPORT(R.string.category_transport, CategoryTransport, android.R.drawable.ic_menu_directions),
    ENTERTAINMENT(R.string.category_entertainment, CategoryEntertainment, android.R.drawable.ic_media_play),
    HOUSING(R.string.category_housing, CategoryHousing, android.R.drawable.ic_menu_myplaces),
    BILLS(R.string.category_bills, CategoryBills, android.R.drawable.ic_menu_agenda),
    HEALTH(R.string.category_health, CategoryHealth, android.R.drawable.ic_menu_info_details),
    SHOPPING(R.string.category_shopping, CategoryShopping, android.R.drawable.ic_menu_crop),
    EDUCATION(R.string.category_education, CategoryEducation, android.R.drawable.ic_menu_edit),
    TRAVEL(R.string.category_travel, CategoryTravel, android.R.drawable.ic_menu_compass),
    OTHER(R.string.category_other, CategoryOther, android.R.drawable.ic_menu_help);

    companion object {
        fun fromString(raw: String?): ExpenseCategory {
            val normalized = raw?.trim().orEmpty()
            if (normalized.isEmpty()) return OTHER

            val normalizedLower = normalized.lowercase(Locale.US)
            return entries.firstOrNull { entry ->
                entry.name.lowercase(Locale.US) == normalizedLower
            } ?: OTHER
        }
    }
}
