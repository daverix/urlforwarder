package net.daverix.urlforward.ui

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import net.daverix.urlforward.EditFilterViewModel
import net.daverix.urlforward.EditingState
import net.daverix.urlforward.R
import net.daverix.urlforward.SaveFilterState

@Preview
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun PreviewEditFilter(
    @PreviewParameter(SaveFilterStatePreviewParameterProvider::class) state: SaveFilterState
) {
    var editingState by remember { mutableStateOf(EditingState.EDITING) }

    UrlForwarderTheme {
        EditFilterScreen(
            state = state,
            onCancel = {},
            onSave = {
                editingState = EditingState.SAVING
            },
            onUpdateEncodeUrl = {},
            onUpdateReplaceSubject = {},
            onUpdateReplaceText = {},
            onUpdateFilterUrl = {},
            onUpdateName = {},
            onUpdateRegex = {},
            onDelete = {
                editingState = EditingState.DELETING
            }
        )
    }
}

fun NavController.navigateToEditFilter(id: Long) {
    navigate("filters/$id")
}

fun NavGraphBuilder.editFilterScreen(
    onNavigateUp: () -> Unit
) {
    composable(
        route = "filters/{filterId}",
        arguments = listOf(
            navArgument("filterId") { type = NavType.LongType }
        )
    ) {
        EditFilterScreen(onClose = onNavigateUp)
    }
}

@Composable
fun EditFilterScreen(
    viewModel: EditFilterViewModel = hiltViewModel(),
    onClose: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val editingState = (state as? SaveFilterState.Editing)?.editingState
    LaunchedEffect(state) {
        if(editingState == EditingState.SAVED || editingState == EditingState.DELETED) {
            onClose()
        }
    }

    EditFilterScreen(
        state = state,
        onSave = viewModel::save,
        onCancel = onClose,
        onUpdateName = viewModel::updateName,
        onUpdateRegex = viewModel::updateRegex,
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
    onUpdateRegex: (String) -> Unit,
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
    }) { padding ->
        EditFilterContent(
            state = state,
            contentPadding = padding,
            onUpdateName = onUpdateName,
            onUpdateRegex = onUpdateRegex,
            onUpdateFilterUrl = onUpdateFilterUrl,
            onUpdateReplaceText = onUpdateReplaceText,
            onUpdateReplaceSubject = onUpdateReplaceSubject,
            onUpdateEncodeUrl = onUpdateEncodeUrl,
            onDelete = onDelete
        )
    }
}

@Composable
private fun EditFilterContent(
    state: SaveFilterState,
    contentPadding: PaddingValues,
    onUpdateName: (String) -> Unit,
    onUpdateRegex: (String) -> Unit,
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
            contentPadding = contentPadding,
            onUpdateName = onUpdateName,
            onUpdateRegex = onUpdateRegex,
            onUpdateFilterUrl = onUpdateFilterUrl,
            onUpdateReplaceText = onUpdateReplaceText,
            onUpdateReplaceSubject = onUpdateReplaceSubject,
            onUpdateEncodeUrl = onUpdateEncodeUrl,
            onDelete = onDelete
        )
        SaveFilterState.Loading -> Box(
            modifier = modifier
                .padding(contentPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
private fun EditFilterFields(
    state: SaveFilterState.Editing,
    contentPadding: PaddingValues,
    onUpdateName: (String) -> Unit,
    onUpdateRegex: (String) -> Unit,
    onUpdateFilterUrl: (String) -> Unit,
    onUpdateReplaceText: (String) -> Unit,
    onUpdateReplaceSubject: (String) -> Unit,
    onUpdateEncodeUrl: (Boolean) -> Unit,
    onDelete: () -> Unit
) {
    var showDialog: Boolean by remember { mutableStateOf(false) }

    FilterFields(
        state = state,
        contentPadding = contentPadding,
        onUpdateName = onUpdateName,
        onUpdateRegex = onUpdateRegex,
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

@OptIn(ExperimentalComposeUiApi::class)
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
