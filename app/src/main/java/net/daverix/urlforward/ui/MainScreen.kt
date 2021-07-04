package net.daverix.urlforward.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
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
        val scope = rememberCoroutineScope()
        scope.launch {
            viewModel.actions.collect {
                when (it) {
                    SaveFilterAction.Cancel -> navController.navigateUp()
                    SaveFilterAction.CloseSuccessfully -> navController.navigateUp()
                }
            }
        }
        AddFilterScreen(
            viewModel = viewModel,
            onCancel = {
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
        val scope = rememberCoroutineScope()
        scope.launch {
            viewModel.actions.collect {
                when (it) {
                    SaveFilterAction.Cancel -> navController.navigateUp()
                    SaveFilterAction.CloseSuccessfully -> navController.navigateUp()
                }
            }
        }

        EditFilterScreen(
            viewModel = viewModel,
            onCancel = {
                navController.navigateUp()
            }
        )
    }
}
