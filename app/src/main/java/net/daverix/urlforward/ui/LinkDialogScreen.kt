package net.daverix.urlforward.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.daverix.urlforward.*
import net.daverix.urlforward.R

@Composable
fun LinkDialogScreen(
    state: DialogState,
    onItemClick: (String) -> Unit
) {
    Surface {
        Column {
            Row(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .height(64.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.choose_filter),
                    fontSize = MaterialTheme.typography.h6.fontSize,
                    modifier = Modifier.paddingFromBaseline(top = 40.dp)
                )
            }

            when (state) {
                is DialogState.Filters -> {
                    FilterList(state.filters, onItemClick)
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
private fun FilterList(
    filters: List<LinkDialogListItem>,
    onItemClick: (String) -> Unit
) {
    LazyColumn(contentPadding = PaddingValues(bottom = 24.dp)) {
        items(filters) {
            val modifier = Modifier
                .padding(horizontal = 24.dp)
                .height(64.dp)
                .fillParentMaxWidth()

            ListItem(
                item = it,
                modifier = when {
                    it.hasMatchingApp -> modifier.clickable {
                        onItemClick(it.url)
                    }
                    else -> modifier
                }
            )
        }
    }
}

@Composable
private fun ListItem(
    item: LinkDialogListItem,
    modifier: Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = item.name,
            style = MaterialTheme.typography.body1,
            color = when {
                item.hasMatchingApp -> Color.Unspecified
                else -> MaterialTheme.colors.error
            },
            fontWeight = FontWeight.Bold,
            modifier = Modifier.paddingFromBaseline(top = 28.dp),
            maxLines = 1
        )
        Text(
            text = item.url,
            style = MaterialTheme.typography.body2,
            modifier = Modifier.paddingFromBaseline(top = 20.dp),
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

    UrlForwarderTheme(darkTheme = false) {
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

    UrlForwarderTheme(darkTheme = true) {
        LinkDialogScreen(
            state = DialogState.Filters(filters),
            onItemClick = {}
        )
    }
}
