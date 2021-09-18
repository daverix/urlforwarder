package net.daverix.urlforward.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import net.daverix.urlforward.*
import net.daverix.urlforward.db.FilterDao

@ExperimentalComposeUiApi
@Composable
fun MainScreen(filterDao: FilterDao) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "filters"
    ) {
        addFilters(filterDao, navController)
        addCreateFilter(filterDao, navController)
        addEditFilter(filterDao, navController)
    }
}

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
            }
        )
    }
}

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
