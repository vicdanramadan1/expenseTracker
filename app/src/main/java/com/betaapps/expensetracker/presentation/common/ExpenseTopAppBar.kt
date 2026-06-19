package com.betaapps.expensetracker.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.betaapps.expensetracker.R

@Composable
fun ExpenseTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    onMenuClick: () -> Unit = {},
    height: Dp = 64.dp,
    horizontalPadding: Dp = 24.dp,
    titleHorizontalPadding: Dp = 58.dp,
    containerColor: Color = Color.Transparent,
    titleColor: Color? = null,
    titleStyle: TextStyle? = null,
    menuIconSize: Dp = 28.dp,
    navigationIcon: (@Composable BoxScope.() -> Unit)? = null,
    endContent: @Composable BoxScope.() -> Unit = {}
) {
    val resolvedTitleColor = titleColor ?: MaterialTheme.colorScheme.onSurface
    val resolvedTitleStyle = titleStyle ?: MaterialTheme.typography.headlineMedium.copy(
        fontSize = 24.sp,
        lineHeight = 28.sp,
        fontWeight = FontWeight.ExtraBold
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .background(containerColor)
            .padding(horizontal = horizontalPadding)
    ) {
        Box(
            modifier = Modifier.align(Alignment.CenterStart),
            contentAlignment = Alignment.CenterStart
        ) {
            val customNavigationIcon = navigationIcon
            if (customNavigationIcon == null) {
                ExpenseTopAppBarIconButton(
                    imageVector = Icons.Default.Menu,
                    contentDescription = stringResource(R.string.home_menu_content_description),
                    onClick = onMenuClick,
                    iconSize = menuIconSize
                )
            } else {
                customNavigationIcon()
            }
        }

        Text(
            text = title,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = titleHorizontalPadding),
            color = resolvedTitleColor,
            style = resolvedTitleStyle,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Box(
            modifier = Modifier.align(Alignment.CenterEnd),
            contentAlignment = Alignment.CenterEnd
        ) {
            endContent()
        }
    }
}

@Composable
fun ExpenseTopAppBarIconButton(
    imageVector: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    tint: Color? = null,
    iconSize: Dp = 28.dp,
    buttonSize: Dp = 44.dp
) {
    IconButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.size(buttonSize)
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = tint ?: MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(iconSize)
        )
    }
}
