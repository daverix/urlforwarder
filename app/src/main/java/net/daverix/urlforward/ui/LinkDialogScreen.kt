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
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.daverix.urlforward.DialogState
import net.daverix.urlforward.LinkFilter
import net.daverix.urlforward.R

@Composable
fun LinkDialogScreen(
    state: DialogState,
    onItemClick: (LinkFilter) -> Unit
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
                    LazyColumn(contentPadding = PaddingValues(bottom = 24.dp)) {
                        items(state.filters) {
                            Row(
                                modifier = Modifier
                                    .height(48.dp)
                                    .fillParentMaxWidth()
                                    .clickable {
                                        onItemClick(it)
                                    }
                            ) {
                                Text(
                                    text = it.name,
                                    style = MaterialTheme.typography.body1,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .padding(horizontal = 24.dp)
                                        .align(CenterVertically)
                                )
                            }
                        }
                    }
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

private fun createFilter(
    title: String,
    filterUrl: String
) = LinkFilter(
    name = title,
    filterUrl = filterUrl,
    replaceText = "@url",
    replaceSubject = "@subject",
    created = 0,
    updated = 0,
    encoded = true
)

@Preview(showBackground = true)
@Composable
private fun LinkDialogScreenPreview() {
    val filters = List(10) {
        createFilter("My filter ${it+1}", "http://example.com/?url=@url&something=$it")
    }

    UrlForwarderTheme(darkTheme = true) {
        LinkDialogScreen(
            state = DialogState.Filters(filters),
            onItemClick = {}
        )
    }
}
