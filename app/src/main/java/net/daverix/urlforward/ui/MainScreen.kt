package net.daverix.urlforward.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import net.daverix.urlforward.*
import net.daverix.urlforward.db.FilterDao

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@Composable
fun MainScreen(filterDao: FilterDao) {
    val navController = rememberAnimatedNavController()

    AnimatedNavHost(
        navController = navController,
        startDestination = "filters"
    ) {
        addFilters(filterDao, navController)
        addCreateFilter(filterDao, navController)
        addEditFilter(filterDao, navController)
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addFilters(
    filterDao: FilterDao,
    navController: NavHostController
) {
    composable(
        route = "filters"
    ) {
        val viewModel = viewModelWithFactory {
            FiltersViewModel(filterDao = filterDao)
        }
        FiltersScreen(
            viewModel = viewModel,
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
    filterDao: FilterDao,
    navController: NavHostController
) {
    composable(
        route = "add-filter"
    ) {
        val viewModel = viewModelWithFactory {
            CreateFilterViewModel(filterDao = filterDao)
        }
        AddFilterScreen(
            viewModel = viewModel,
            onClose = {
                navController.navigateUp()
            },
            onSaved = {
                navController.navigate("filters")
            }
        )
    }
}

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
private fun NavGraphBuilder.addEditFilter(
    filterDao: FilterDao,
    navController: NavHostController
) {
    composable(
        route = "filters/{filterId}",
        arguments = listOf(
            navArgument("filterId") { type = NavType.LongType }
        )
    ) { backStackEntry ->
        val viewModel = viewModelWithFactory {
            EditFilterViewModel(
                filterDao = filterDao,
                filterId = backStackEntry.arguments?.getLong("filterId")
                    ?: error("no filterId provided")
            )
        }

        EditFilterScreen(
            viewModel = viewModel,
            onClose = {
                navController.navigateUp()
            }
        )
    }
}
