package net.daverix.urlforward.ui

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import net.daverix.urlforward.FiltersState
import net.daverix.urlforward.FiltersViewModel
import net.daverix.urlforward.LinkFilter
import net.daverix.urlforward.R
import net.daverix.urlforward.createInitialAddFilter

private class FilterStatePreviewParameterProvider : PreviewParameterProvider<FiltersState> {
    override val values: Sequence<FiltersState> = sequenceOf(
        FiltersState.Loading,
        FiltersState.LoadedFilters(emptyList()),
        FiltersState.LoadedFilters((0 until 20).map {
            createInitialAddFilter(
                id = it.toLong(),
                name = "Filter $it"
            )
        })
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun FiltersLoadingPreview(
    @PreviewParameter(FilterStatePreviewParameterProvider::class) state: FiltersState
) {
    UrlForwarderTheme {
        SharedTransitionLayout {
            AnimatedVisibility(visible = true) {
                CompositionLocalProvider(
                    LocalAnimationScope provides this,
                    LocalSharedTransitionScope provides this@SharedTransitionLayout
                ) {
                    Filters(
                        state = state,
                        onItemClicked = { },
                        onAddItem = { }
                    )
                }
            }
        }
    }
}

fun NavGraphBuilder.filtersScreen(
    onNavigateToFilter: (id: Long) -> Unit,
    onAddFilter: () -> Unit
) {
    composable(
        route = "filters"
    ) {
        CompositionLocalProvider(LocalAnimationScope provides this) {
            FiltersScreen(
                onItemClicked = onNavigateToFilter,
                onAddItem = onAddFilter
            )
        }
    }
}

@Composable
fun FiltersScreen(
    viewModel: FiltersViewModel = hiltViewModel(),
    onItemClicked: (id: Long)->Unit,
    onAddItem: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    Filters(
        state = state,
        onItemClicked = onItemClicked,
        onAddItem = onAddItem
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun Filters(
    state: FiltersState,
    onItemClicked: (id: Long) -> Unit,
    onAddItem: () -> Unit
) {
    with(LocalSharedTransitionScope.current) {
        Scaffold(
            topBar = {
                AppBar(
                    title = {
                        Text(text = "Url Forwarder")
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = onAddItem,
                    backgroundColor = MaterialTheme.colors.secondary,
                    contentColor = MaterialTheme.colors.onSecondary,
                    shape = CircleShape,
                    modifier = Modifier
                        .sharedBounds(
                            sharedContentState = rememberSharedContentState(
                                key = "add-filter-bounds"
                            ),
                            animatedVisibilityScope = LocalAnimationScope.current
                        )
                        .navigationBarsPadding()
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = stringResource(id = R.string.create_filter)
                    )
                }
            }
        ) { padding ->
            when (state) {
                is FiltersState.LoadedFilters -> {
                    if (state.filters.isEmpty()) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .padding(padding)
                                .fillMaxSize()
                        ) {
                            Text(
                                text = "No filters added"
                            )
                        }
                    } else {
                        FiltersList(
                            filters = state.filters,
                            contentPadding = padding,
                            onItemClicked = onItemClicked,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                FiltersState.Loading -> Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
private fun FiltersList(
    filters: List<LinkFilter>,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    onItemClicked: (id: Long) -> Unit
) {
    LazyColumn(
        contentPadding = contentPadding,
        modifier = modifier
    ) {
        items(items = filters, key = { it.id }) { item ->
            FilterItem(
                item = item,
                onClick = {
                    onItemClicked(item.id)
                }
            )
        }
        item(key = "footer") {
            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun FilterItem(
    item: LinkFilter,
    onClick: () -> Unit
) {
    with(LocalSharedTransitionScope.current) {
        Column(
            modifier = Modifier
                .clickable {
                    onClick()
                }
                .padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                )
                .fillMaxWidth()
        ) {
            Text(
                text = item.name,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.sharedBounds(
                    sharedContentState = rememberSharedContentState(key = "edit-filter-name-${item.id}"),
                    animatedVisibilityScope = LocalAnimationScope.current
                )
            )
            Text(
                text = item.filterUrl,
                modifier = Modifier.sharedBounds(
                    sharedContentState = rememberSharedContentState(key = "edit-filter-url-${item.id}"),
                    animatedVisibilityScope = LocalAnimationScope.current
                )
            )
        }
    }
}
