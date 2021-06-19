package net.daverix.urlforward.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import net.daverix.urlforward.DialogState
import net.daverix.urlforward.LinkFilter
import net.daverix.urlforward.R

@Composable
fun LinkDialogScreen(
    state: DialogState,
    onItemClick: (LinkFilter) -> Unit
) {
    UrlForwarderTheme {
        Column {
            Text(text = stringResource(id = R.string.choose_filter))

            when (state) {
                is DialogState.Filters -> {
                    LinkList(
                        state = state,
                        onItemClick = onItemClick
                    )
                }
                DialogState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.testTag("loading")
                    )
                }
            }
        }
    }
}

@Composable
private fun LinkList(
    state: DialogState.Filters,
    onItemClick: (LinkFilter) -> Unit
) {
    LazyColumn {
        items(state.filters) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .clickable {
                        onItemClick(it)
                    }
            ) {
                Text(text = it.title)
            }
        }
    }
}