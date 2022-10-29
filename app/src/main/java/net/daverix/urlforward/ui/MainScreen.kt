package net.daverix.urlforward.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "filters"
    ) {
        filtersScreen(
            onNavigateToFilter = navController::navigateToEditFilter,
            onAddFilter = navController::navigateToAddFilter
        )
        addFilterScreen(
            onNavigateUp = navController::navigateUp
        )
        editFilterScreen(
            onNavigateUp = navController::navigateUp
        )
    }
}
