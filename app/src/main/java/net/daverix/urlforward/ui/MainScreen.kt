package net.daverix.urlforward.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@OptIn(ExperimentalAnimationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun MainScreen() {
    val navController = rememberAnimatedNavController()

    AnimatedNavHost(
        navController = navController,
        startDestination = "filters"
    ) {
        addFilters(navController)
        addCreateFilter(navController)
        addEditFilter(navController)
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addFilters(
    navController: NavHostController
) {
    composable(
        route = "filters"
    ) {
        FiltersScreen(
            onItemClicked = {
                navController.navigate("filters/${it.id}")
            },
            onAddItem = {
                navController.navigate("add-filter")
            }
        )
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addCreateFilter(
    navController: NavHostController
) {
    composable(
        route = "add-filter"
    ) {
        AddFilterScreen(
            onClose = {
                navController.navigateUp()
            }
        )
    }
}

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
private fun NavGraphBuilder.addEditFilter(
    navController: NavHostController
) {
    composable(
        route = "filters/{filterId}",
        arguments = listOf(
            navArgument("filterId") { type = NavType.LongType }
        )
    ) {
        EditFilterScreen(
            onClose = {
                navController.navigateUp()
            }
        )
    }
}
