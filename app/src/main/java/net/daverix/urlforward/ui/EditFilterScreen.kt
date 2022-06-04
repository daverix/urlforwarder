package net.daverix.urlforward.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import net.daverix.urlforward.EditFilterViewModel
import net.daverix.urlforward.LinkFilter
import net.daverix.urlforward.R
import net.daverix.urlforward.SaveFilterState


@ExperimentalComposeUiApi
@Composable
fun EditFilterScreen(
    viewModel: EditFilterViewModel,
    onClose: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(state) {
        if(state is SaveFilterState.Saved) {
            onClose()
        }
    }

    EditFilterScreen(
        state = state,
        onSave = viewModel::save,
        onCancel = onClose,
        onUpdateName = viewModel::updateName,
        onUpdateFilterUrl = viewModel::updateFilterUrl,
        onUpdateReplaceText = viewModel::updateReplaceUrl,
        onUpdateReplaceSubject = viewModel::updateReplaceSubject,
        onUpdateEncodeUrl = viewModel::updateEncoded,
        onDelete = viewModel::delete
    )
}

@ExperimentalComposeUiApi
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
    Scaffold(topBar = {
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
                TextButton(onClick = onSave) {
                    Text(
                        text = stringResource(id = R.string.save),
                        color = MaterialTheme.colors.onPrimary
                    )
                }
            },
            elevation = 8.dp
        )
    }) {
        EditFilterContent(
            state = state,
            onUpdateName = onUpdateName,
            onUpdateFilterUrl = onUpdateFilterUrl,
            onUpdateReplaceText = onUpdateReplaceText,
            onUpdateReplaceSubject = onUpdateReplaceSubject,
            onUpdateEncodeUrl = onUpdateEncodeUrl,
            onDelete = onDelete
        )
    }
}

@ExperimentalComposeUiApi
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
        is SaveFilterState.Editing -> EditFilterFields(
            state = state,
            onUpdateName = onUpdateName,
            onUpdateFilterUrl = onUpdateFilterUrl,
            onUpdateReplaceText = onUpdateReplaceText,
            onUpdateReplaceSubject = onUpdateReplaceSubject,
            onUpdateEncodeUrl = onUpdateEncodeUrl,
            onDelete = onDelete
        )
        SaveFilterState.Loading -> Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

@ExperimentalComposeUiApi
@Composable
private fun EditFilterFields(
    state: SaveFilterState.Editing,
    onUpdateName: (String) -> Unit,
    onUpdateFilterUrl: (String) -> Unit,
    onUpdateReplaceText: (String) -> Unit,
    onUpdateReplaceSubject: (String) -> Unit,
    onUpdateEncodeUrl: (Boolean) -> Unit,
    onDelete: () -> Unit
) {
    var showDialog: Boolean by remember { mutableStateOf(false) }

    FilterFields(
        state = state,
        onUpdateName = onUpdateName,
        onUpdateFilterUrl = onUpdateFilterUrl,
        onUpdateReplaceText = onUpdateReplaceText,
        onUpdateReplaceSubject = onUpdateReplaceSubject,
        onUpdateEncodeUrl = onUpdateEncodeUrl,
        footerContent = {
            DeleteButton(onClick = { showDialog = true })
        }
    )

    if (showDialog) {
        ConfirmDeletionDialog(
            filterName = state.filter.name,
            onDelete = onDelete,
            onCancel = { showDialog = false }
        )
    }
}

@Composable
private fun DeleteButton(onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth()
    ) {
        Button(onClick = onClick) {
            Text(text = stringResource(id = R.string.delete))
        }
    }
}

@ExperimentalComposeUiApi
@Composable
private fun ConfirmDeletionDialog(
    filterName: String,
    onDelete: () -> Unit,
    onCancel: () -> Unit
) {
    AlertDialog(
        title = {
            Text(text = stringResource(id = R.string.dialog_confirm_delete, filterName))
        },
        onDismissRequest = onCancel,
        confirmButton = {
            TextButton(
                onClick = onDelete
            ) {
                Text(text = stringResource(id = R.string.delete))
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text(text = stringResource(id = android.R.string.cancel))
            }
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = true
        )
    )
}

@ExperimentalComposeUiApi
@Preview(showBackground = true)
@Composable
private fun PreviewDeleteDialog() {
    UrlForwarderTheme(darkTheme = false) {
        ConfirmDeletionDialog(
            onDelete = {},
            onCancel = {},
            filterName = "My filter"
        )
    }
}

@ExperimentalComposeUiApi
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

@ExperimentalComposeUiApi
@Preview(showBackground = true)
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
    name = "Test",
    filterUrl = "https://example.com/?url=@url&subject=@url",
    replaceText = "@url",
    replaceSubject = "@subject",
    created = 0L,
    updated = 0L,
    encoded = true
)