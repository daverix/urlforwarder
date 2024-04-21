package net.daverix.urlforward.ui

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun AppBar(
    title: @Composable () -> Unit,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    TopAppBar(
        windowInsets = AppBarDefaults.topAppBarWindowInsets,
        title = title,
        navigationIcon = navigationIcon,
        actions = actions,
        elevation = 8.dp
    )
}