package net.daverix.urlforward.ui

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.background
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    SharedTransitionLayout {
        CompositionLocalProvider(LocalSharedTransitionScope provides this) {
            NavHost(
                navController = navController,
                startDestination = "filters",
                modifier = Modifier.background(MaterialTheme.colors.background),
            ) {
                filtersScreen(
                    onNavigateToFilter = navController::navigateToEditFilter,
                    onAddFilter = navController::navigateToAddFilter,
                )
                addFilterScreen(
                    onNavigateUp = navController::navigateUp,
                )
                editFilterScreen(
                    onNavigateUp = navController::navigateUp,
                )
            }
        }
    }
}
