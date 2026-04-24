package com.betaapps.expensetracker.presentation.feature.main

import androidx.annotation.StringRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.betaapps.expensetracker.R
import com.betaapps.expensetracker.presentation.feature.analytics.AnalyticsScreen
import com.betaapps.expensetracker.presentation.feature.home.HomeScreen
import com.betaapps.expensetracker.presentation.feature.home.model.Expense
import com.betaapps.expensetracker.presentation.feature.profile.ProfileScreen
import com.betaapps.expensetracker.ui.theme.BudgetSafe
import com.betaapps.expensetracker.ui.theme.TextSecondary

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
                containerColor = Color.White,
                modifier = Modifier.shadow(    elevation = 10.dp,
                    shape = RoundedCornerShape(18.dp),
                    ambientColor = MaterialTheme.colorScheme.scrim,
                    spotColor = MaterialTheme.colorScheme.scrim)
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
                        colors = NavigationBarItemColors(
                            selectedIndicatorColor = MaterialTheme.colorScheme.primary,
                            selectedIconColor = Color.White,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary,
                            disabledIconColor =Color.White,
                            disabledTextColor = Color.White
                        ),
                        label = { Text(text = stringResource(tab.labelRes)) }
                    )
                }
            }
        }
    ) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)

        ) {
            val headerHeight = maxHeight * 0.25f
            val overlap = headerHeight * 0.40f
            val contentTopPadding = headerHeight - overlap

            Box(
                modifier = Modifier.fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                MainContainerHeader(
                    title = stringResource(selectedTab.labelRes),
                    headerHeight = headerHeight,
                    modifier = Modifier.align(Alignment.TopCenter)
                )

                val contentModifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = contentTopPadding,
                        bottom = 16.dp,
                        start = 16.dp,
                        end = 16.dp
                    )

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
    }
}

@Composable
private fun MainContainerHeader(
    title: String,
    headerHeight: Dp,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)
    val accent = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.10f)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(headerHeight)
            .background(BudgetSafe, shape)
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val path = Path().apply {
                moveTo(size.width * 0.70f, 0f)
                lineTo(size.width, 0f)
                lineTo(size.width * 0.78f, size.height)
                lineTo(size.width * 0.48f, size.height)
                close()
            }
            drawPath(path = path, color = accent)
        }

        Text(
            text = title,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 48.dp)
            ,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}
