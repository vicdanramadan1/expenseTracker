package com.betaapps.expensetracker.presentation.feature.main

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.betaapps.expensetracker.R
import com.betaapps.expensetracker.presentation.feature.analytics.AnalyticsScreen
import com.betaapps.expensetracker.presentation.feature.home.HomeScreen
import com.betaapps.expensetracker.presentation.feature.home.model.Expense
import com.betaapps.expensetracker.presentation.feature.profile.ProfileScreen

private enum class MainTab(
    @StringRes val labelRes: Int,
    val iconRes: ImageVector
) {
    Home(R.string.tab_home, Icons.Default.Home),
    Analytics(R.string.tab_analytics, Icons.Default.Analytics),
    Profile(R.string.tab_profile, Icons.Default.Person)
}

@Composable
fun MainContainerScreen(
    onAddExpenseClick: () -> Unit,
    onSearchClick: () -> Unit,
    onExpenseClick: (Expense) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by rememberSaveable { mutableStateOf(MainTab.Home) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                containerColor = Color.White
            ) {
                MainTab.entries.forEach { tab ->
                    NavigationBarItem(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        icon = {
                            Icon(
                                imageVector = tab.iconRes,
                                contentDescription = null
                            )
                        },
                        label = { Text(text = stringResource(tab.labelRes)) }
                    )
                }
            }
        }
    ) { innerPadding ->
        val contentModifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)

        when (selectedTab) {
            MainTab.Home -> HomeScreen(
                modifier = contentModifier,
                onAddExpenseClick = onAddExpenseClick,
                onSearchClick = onSearchClick,
                onExpenseClick = onExpenseClick
            )

            MainTab.Analytics -> AnalyticsScreen(modifier = contentModifier)
            MainTab.Profile -> ProfileScreen(modifier = contentModifier)
        }
    }
}