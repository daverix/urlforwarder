package net.daverix.urlforward.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.daverix.urlforward.FiltersState
import net.daverix.urlforward.FiltersViewModel
import net.daverix.urlforward.LinkFilter
import net.daverix.urlforward.R

@Composable
fun FiltersScreen(
    viewModel: FiltersViewModel,
    onItemClicked: (LinkFilter)->Unit,
    onAddItem: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    Filters(
        state = state,
        onItemClicked = onItemClicked,
        onAddItem = onAddItem
    )
}

@Composable
private fun Filters(
    state: FiltersState,
    onItemClicked: (LinkFilter) -> Unit,
    onAddItem: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
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
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(id = R.string.create_filter)
                )
            }
        }
    ) {
        when (state) {
            is FiltersState.LoadedFilters -> {
                if (state.filters.isEmpty()) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = "No filters added"
                        )
                    }
                } else {
                    FiltersList(
                        filters = state.filters,
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

@Composable
private fun FiltersList(
    filters: List<LinkFilter>,
    onItemClicked: (LinkFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(vertical = 8.dp),
        modifier = modifier
    ) {
        items(filters) { item ->
            FilterItem(
                item = item,
                onClick = {
                    onItemClicked(item)
                }
            )
        }
    }
}

@Composable
private fun FilterItem(
    item: LinkFilter,
    onClick: () -> Unit
) {
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
            fontWeight = FontWeight.Bold
        )
        Text(
            text = item.filterUrl
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FiltersLoadingPreview() {
    UrlForwarderTheme {
        Filters(
            state = FiltersState.Loading,
            onItemClicked = {

            },
            onAddItem = {

            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FiltersLoadedPreview() {
    UrlForwarderTheme {
        Filters(
            state = FiltersState.LoadedFilters(
                listOf(
                    LinkFilter(
                        name = "Testing",
                        filterUrl = "http://example.com?url=@url&subject=@subject",
                        replaceText = "@url",
                        replaceSubject = "@subject",
                        created = 0,
                        updated = 0,
                        encoded = true
                    )
                )
            ),
            onItemClicked = {},
            onAddItem = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FiltersEmptyPreview() {
    UrlForwarderTheme {
        Filters(
            state = FiltersState.LoadedFilters(emptyList()),
            onItemClicked = {},
            onAddItem = {}
        )
    }
}