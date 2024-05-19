package net.daverix.urlforward.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import net.daverix.urlforward.DialogState
import net.daverix.urlforward.LinkDialogListItem
import net.daverix.urlforward.LinkDialogViewModel
import net.daverix.urlforward.R

@Composable
fun LinkDialogScreen(
    url: String,
    subject: String?,
    viewModel: LinkDialogViewModel = hiltViewModel(),
    onItemClick: (String) -> Unit
) {
    LaunchedEffect(url, subject) {
        viewModel.load(url, subject)
    }

    val state by viewModel.state.collectAsState()
    LinkDialogScreen(
        state = state,
        onItemClick = onItemClick
    )
}

@Composable
fun LinkDialogScreen(
    state: DialogState,
    onItemClick: (String) -> Unit
) {
    Card(
        modifier = Modifier.padding(16.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column {
            Text(
                text = stringResource(id = R.string.choose_filter),
                style = MaterialTheme.typography.h5,
                modifier = Modifier.fillMaxWidth()
                    .wrapContentSize(Alignment.Center)
                    .padding(top = 16.dp)
            )
            Crossfade(
                targetState = state,
                label = "filtersFade"
            ) { currentState ->
                when (currentState) {
                    is DialogState.Filters -> FilterList(currentState.filters, onItemClick)

                    DialogState.Loading -> CircularProgressIndicator(
                        modifier = Modifier.testTag("loading")
                    )
                }
            }
        }
    }
}

@Composable
private fun FilterList(
    filters: List<LinkDialogListItem>,
    onItemClick: (String) -> Unit
) {
    LazyColumn(contentPadding = PaddingValues(vertical = 12.dp)) {
        items(filters) { item ->
            ListItem(
                item = item,
                onItemClick = onItemClick
            )
        }
    }
}

@Composable
private fun ListItem(
    item: LinkDialogListItem,
    modifier: Modifier = Modifier,
    onItemClick: (String) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = item.hasMatchingApp) {
                onItemClick(item.url)
            }
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        Text(
            text = item.name,
            style = MaterialTheme.typography.body1,
            color = when {
                item.hasMatchingApp -> Color.Unspecified
                else -> MaterialTheme.colors.error
            },
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )
        Text(
            text = item.url,
            style = MaterialTheme.typography.body2,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun LinkDialogScreenPreview() {
    val filters = List(10) {
        LinkDialogListItem(
            name = "My filter ${it + 1}",
            url = "http://example.com/?url=@url&something=$it",
            hasMatchingApp = it > 0
        )
    }

    UrlForwarderTheme(
        darkTheme = false,
        transparentBackground = true
    ) {
        LinkDialogScreen(
            state = DialogState.Filters(filters),
            onItemClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LinkDialogScreenPreviewDark() {
    val filters = List(10) {
        LinkDialogListItem(
            name = "My filter ${it + 1}",
            url = "http://example.com/?url=@url&something=$it",
            hasMatchingApp = it > 0
        )
    }

    UrlForwarderTheme(
        darkTheme = true,
        transparentBackground = true
    ) {
        LinkDialogScreen(
            state = DialogState.Filters(filters),
            onItemClick = {}
        )
    }
}
