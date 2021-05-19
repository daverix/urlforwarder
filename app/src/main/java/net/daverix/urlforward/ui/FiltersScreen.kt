package net.daverix.urlforward.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.daverix.urlforward.FiltersViewModel
import net.daverix.urlforward.LinkFilter

@Composable
fun FiltersScreen(viewModel: FiltersViewModel) {
    UrlForwarderTheme {
        Filters(
            filters = viewModel.filters,
            onItemClicked = viewModel::editItem,
            onAddItem = viewModel::addItem)
    }
}

@Composable
private fun Filters(
    filters: List<LinkFilter>,
    onItemClicked: (LinkFilter) -> Unit,
    onAddItem: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Text(text = "Url Forwarder")
            }
        )

        BoxWithConstraints(modifier = Modifier.weight(1f)) {
            Surface {
                if (filters.isNullOrEmpty()) {
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
                        filters = filters,
                        onItemClicked = onItemClicked
                    )
                }
            }
            FloatingActionButton(
                onClick = onAddItem,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                backgroundColor = MaterialTheme.colors.secondary,
                contentColor = MaterialTheme.colors.onSecondary,
                shape = CircleShape,
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add filter")
            }
        }
    }
}

@Composable
private fun FiltersList(
    filters: List<LinkFilter>,
    onItemClicked: (LinkFilter) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
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
    Text(
        text = item.title,
        modifier = Modifier.clickable {
            onClick()
        }
    )
}

@Preview(showBackground = true)
@Composable
fun FiltersPreview() {
    var items by remember { mutableStateOf<List<LinkFilter>>(emptyList()) }
    UrlForwarderTheme {
        Filters(
            filters = items,
            onItemClicked = {

            },
            onAddItem = {
                //TODO: open add filter screen, add it to back stack
                items = items + LinkFilter(
                    title = "Hello World",
                    outputUrl = "http://asdasdasd.com/?text=@hej",
                    urlPattern = "@hej",
                    subjectPattern = ""
                )
            }
        )
    }
}