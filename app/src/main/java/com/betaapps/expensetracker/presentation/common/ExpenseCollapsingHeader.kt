package com.betaapps.expensetracker.presentation.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import com.betaapps.expensetracker.R
import com.betaapps.expensetracker.ui.theme.BudgetSafe

@Immutable
data class CollapsingHeaderState(
    val height: Dp,
    val collapseFraction: Float
)

@Composable
fun rememberCollapsingHeaderState(
    scrollState: ScrollState,
    expandedHeight: Dp = 126.dp,
    collapsedHeight: Dp = 64.dp
): CollapsingHeaderState {
    val density = LocalDensity.current
    val collapseFraction by remember(scrollState, expandedHeight, collapsedHeight, density) {
        derivedStateOf {
            val collapseRangePx = with(density) {
                (expandedHeight - collapsedHeight).toPx().coerceAtLeast(1f)
            }
            (scrollState.value / collapseRangePx).coerceIn(0f, 1f)
        }
    }

    return CollapsingHeaderState(
        height = lerp(expandedHeight, collapsedHeight, collapseFraction),
        collapseFraction = collapseFraction
    )
}

@Composable
fun ExpenseCollapsingHeader(
    title: String,
    state: CollapsingHeaderState,
    modifier: Modifier = Modifier,
    onMenuClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {}
) {
    val expandedContentAlpha = (1f - (state.collapseFraction * 1.35f)).coerceIn(0f, 1f)
    val toolbarTopPadding = lerp(22.dp, 4.dp, state.collapseFraction)
    val titleFontSize = (24f - (4f * state.collapseFraction)).sp
    val titleLineHeight = (28f - (4f * state.collapseFraction)).sp
    val menuIconSize = lerp(28.dp, 24.dp, state.collapseFraction)
    val notificationIconSize = lerp(26.dp, 23.dp, state.collapseFraction)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(state.height)
            .background(Color(0xFFFBFDFC))
    ) {
        Canvas(
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer { alpha = expandedContentAlpha }
        ) {
            val leftWave = Path().apply {
                moveTo(0f, size.height * 0.96f)
                cubicTo(
                    size.width * 0.20f,
                    size.height * 0.82f,
                    size.width * 0.36f,
                    size.height * 1.04f,
                    size.width * 0.52f,
                    size.height * 0.70f
                )
                cubicTo(
                    size.width * 0.66f,
                    size.height * 0.40f,
                    size.width * 0.80f,
                    size.height * 0.60f,
                    size.width,
                    size.height * 0.50f
                )
                lineTo(size.width, size.height)
                lineTo(0f, size.height)
                close()
            }
            drawPath(path = leftWave, color = BudgetSafe.copy(alpha = 0.08f))

            val rightWave = Path().apply {
                moveTo(size.width * 0.86f, size.height)
                cubicTo(
                    size.width * 0.91f,
                    size.height * 0.76f,
                    size.width * 0.96f,
                    size.height * 0.70f,
                    size.width,
                    size.height * 0.62f
                )
                lineTo(size.width, size.height)
                close()
            }
            drawPath(path = rightWave, color = BudgetSafe.copy(alpha = 0.08f))

            val dotColor = Color(0xFFCBD5D1).copy(alpha = 0.48f)
            val startX = size.width * 0.64f
            val startY = size.height * 0.62f
            for (row in 0..2) {
                for (column in 0..5) {
                    drawCircle(
                        color = dotColor,
                        radius = 3.dp.toPx(),
                        center = Offset(
                            x = startX + column * 24.dp.toPx(),
                            y = startY + row * 22.dp.toPx()
                        )
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = toolbarTopPadding)
        ) {
            IconButton(
                onClick = onMenuClick,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(44.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = stringResource(R.string.home_menu_content_description),
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(menuIconSize)
                )
            }

            Text(
                text = title,
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.headlineMedium,
                fontSize = titleFontSize,
                lineHeight = titleLineHeight,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            IconButton(
                onClick = onNotificationsClick,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(44.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.NotificationsNone,
                    contentDescription = stringResource(R.string.home_notifications_content_description),
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(notificationIconSize)
                )
            }
        }
    }
}