package net.daverix.urlforward.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.daverix.urlforward.*
import net.daverix.urlforward.R


@Composable
fun EditFilterScreen(
    viewModel: EditFilterViewModel,
    onCancel: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    EditFilterScreen(
        state = state,
        onSave = viewModel::save,
        onCancel = onCancel,
        onUpdateName = viewModel::updateTitle,
        onUpdateFilterUrl = viewModel::updateFilterUrl,
        onUpdateReplaceText = viewModel::updateReplaceUrl,
        onUpdateReplaceSubject = viewModel::updateReplaceSubject,
        onUpdateEncodeUrl = viewModel::updateEncoded,
        onDelete = viewModel::delete
    )
}

@Composable
private fun EditFilterScreen(
    state: SaveFilterState,
    onCancel: () -> Unit,
    onSave: () -> Unit,
    onDelete: () -> Unit,
    onUpdateName: (String) -> Unit,
    onUpdateFilterUrl: (String) -> Unit,
    onUpdateReplaceText: (String) -> Unit,
    onUpdateReplaceSubject: (String) -> Unit,
    onUpdateEncodeUrl: (Boolean) -> Unit
) {
    UrlForwarderTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.edit_filter))
                },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = stringResource(id = android.R.string.cancel)
                        )
                    }
                },
                actions = {
                    TextButton(
                        onClick = onSave
                    ) {
                        Text(
                            text = stringResource(id = R.string.save),
                            color = MaterialTheme.colors.onPrimary
                        )
                    }
                },
                elevation = 8.dp
            )

            EditFilterContent(
                state = state,
                modifier = Modifier.weight(1f),
                onUpdateName = onUpdateName,
                onUpdateFilterUrl = onUpdateFilterUrl,
                onUpdateReplaceText = onUpdateReplaceText,
                onUpdateReplaceSubject = onUpdateReplaceSubject,
                onUpdateEncodeUrl = onUpdateEncodeUrl,
                onDelete = onDelete
            )
        }
    }
}

@Composable
private fun EditFilterContent(
    state: SaveFilterState,
    onUpdateName: (String) -> Unit,
    onUpdateFilterUrl: (String) -> Unit,
    onUpdateReplaceText: (String) -> Unit,
    onUpdateReplaceSubject: (String) -> Unit,
    onUpdateEncodeUrl: (Boolean) -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (state) {
        is SaveFilterState.Editing ->
            FilterFields(
                state = state,
                onUpdateName = onUpdateName,
                onUpdateFilterUrl = onUpdateFilterUrl,
                onUpdateReplaceText = onUpdateReplaceText,
                onUpdateReplaceSubject = onUpdateReplaceSubject,
                onUpdateEncodeUrl = onUpdateEncodeUrl,
                footerContent = {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.padding(top = 16.dp).fillMaxWidth()
                    ) {
                        Button(onClick = onDelete) {
                            Text(text = stringResource(id = R.string.delete))
                        }
                    }
                }
            )
        SaveFilterState.Loading ->
            Box(
                modifier = modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
    }
}


@Preview(showBackground = true)
@Composable
private fun PreviewEditFilter() {
    UrlForwarderTheme(darkTheme = false) {
        EditFilterScreen(
            state = SaveFilterState.Editing(createPreviewFilter()),
            onCancel = {},
            onSave = {},
            onUpdateEncodeUrl = {},
            onUpdateReplaceSubject = {},
            onUpdateReplaceText = {},
            onUpdateFilterUrl = {},
            onUpdateName = {},
            onDelete = {}
        )
    }
}

@Preview
@Composable
private fun PreviewEditFilterDark() {
    UrlForwarderTheme(darkTheme = true) {
        EditFilterScreen(
            state = SaveFilterState.Editing(createPreviewFilter()),
            onCancel = {},
            onSave = {},
            onUpdateEncodeUrl = {},
            onUpdateReplaceSubject = {},
            onUpdateReplaceText = {},
            onUpdateFilterUrl = {},
            onUpdateName = {},
            onDelete = {}
        )
    }
}

private fun createPreviewFilter() = LinkFilter(
    id = -1,
    title = "Test",
    filterUrl = "https://example.com/?url=@url&subject=@url",
    replaceText = "@url",
    replaceSubject = "@subject",
    created = 0L,
    updated = 0L,
    encoded = true
)