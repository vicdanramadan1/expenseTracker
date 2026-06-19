package com.betaapps.expensetracker.presentation.feature.main

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.betaapps.expensetracker.R
import com.betaapps.expensetracker.presentation.common.ExpenseCollapsingHeader
import com.betaapps.expensetracker.presentation.common.rememberCollapsingHeaderState
import com.betaapps.expensetracker.presentation.feature.analytics.AnalyticsScreen
import com.betaapps.expensetracker.presentation.feature.history.HistoryRoute
import com.betaapps.expensetracker.presentation.feature.home.HomeScreen
import com.betaapps.expensetracker.presentation.feature.home.model.Expense
import com.betaapps.expensetracker.ui.theme.BorderSubtle
import com.betaapps.expensetracker.ui.theme.BudgetSafe
import com.betaapps.expensetracker.ui.theme.TextSecondary

private enum class MainTab(
    @StringRes val labelRes: Int,
    val iconRes: ImageVector
) {
    Home(R.string.tab_home, Icons.Default.Home),
    Analytics(R.string.tab_analytics, Icons.Default.Analytics),
    History(R.string.tab_history, Icons.Default.History),
    Settings(R.string.tab_settings, Icons.Default.Settings)
}

@Composable
fun MainContainerScreen(
    onAddExpenseClick: () -> Unit,
    onSearchClick: () -> Unit,
    onExpenseClick: (Expense) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by rememberSaveable { mutableStateOf(MainTab.Home) }
    val scrollState = rememberScrollState()
    val headerState = rememberCollapsingHeaderState(scrollState = scrollState)
    val selectedTabTitle = stringResource(selectedTab.labelRes)

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color(0xFFF8FAF8),
        bottomBar = {
            HomeBottomBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it },
                onAddExpenseClick = onAddExpenseClick,
                modifier = Modifier.navigationBarsPadding()
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF8FAF8))
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(
                        top = headerState.height + 12.dp,
                        bottom = 22.dp
                    )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 18.dp,
                            end = 18.dp
                        )
                ) {
                    when (selectedTab) {
                        MainTab.Home -> HomeScreen(
                            modifier = Modifier.fillMaxWidth(),
                            onAddExpenseClick = onAddExpenseClick,
                            onSearchClick = onSearchClick,
                            onExpenseClick = onExpenseClick
                        )

                        MainTab.Analytics -> AnalyticsScreen(modifier = Modifier.fillMaxWidth())
                        MainTab.History -> HistoryRoute(
                            modifier = Modifier.fillMaxSize(),
                            onExpenseClick = onExpenseClick
                        )

                        MainTab.Settings -> PlaceholderTabScreen(
                            title = stringResource(R.string.tab_settings),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            ExpenseCollapsingHeader(
                title = selectedTabTitle,
                state = headerState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@Composable
private fun HomeBottomBar(
    selectedTab: MainTab,
    onTabSelected: (MainTab) -> Unit,
    onAddExpenseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(92.dp)
            .padding(horizontal = 18.dp, vertical = 8.dp)
    ) {
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(68.dp)
                .border(1.dp, BorderSubtle.copy(alpha = 0.78f), RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            shadowElevation = 12.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomBarItem(
                    tab = MainTab.Home,
                    selected = selectedTab == MainTab.Home,
                    onClick = { onTabSelected(MainTab.Home) },
                    modifier = Modifier.weight(1f)
                )
                BottomBarItem(
                    tab = MainTab.Analytics,
                    selected = selectedTab == MainTab.Analytics,
                    onClick = { onTabSelected(MainTab.Analytics) },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(76.dp))
                BottomBarItem(
                    tab = MainTab.History,
                    selected = selectedTab == MainTab.History,
                    onClick = { onTabSelected(MainTab.History) },
                    modifier = Modifier.weight(1f)
                )
                BottomBarItem(
                    tab = MainTab.Settings,
                    selected = selectedTab == MainTab.Settings,
                    onClick = { onTabSelected(MainTab.Settings) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        FloatingActionButton(
            onClick = onAddExpenseClick,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .size(64.dp),
            shape = CircleShape,
            containerColor = BudgetSafe,
            contentColor = Color.White
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.home_add_expense_content_description),
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
private fun BottomBarItem(
    tab: MainTab,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val color = if (selected) BudgetSafe else TextSecondary

    Column(
        modifier = modifier
            .fillMaxHeight()
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = tab.iconRes,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = stringResource(tab.labelRes),
            color = color,
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 12.sp,
            lineHeight = 15.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

@Composable
private fun PlaceholderTabScreen(
    title: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.height(420.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = TextSecondary
        )
    }
}
