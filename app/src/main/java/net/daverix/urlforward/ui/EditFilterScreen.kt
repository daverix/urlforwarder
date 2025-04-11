package net.daverix.urlforward.ui

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun PreviewEditFilter(
    @PreviewParameter(SaveFilterStatePreviewParameterProvider::class) state: SaveFilterState
) {
    var editingState by remember { mutableStateOf(EditingState.EDITING) }

    UrlForwarderTheme {
        SharedTransitionLayout {
            AnimatedVisibility(visible = true) {
                CompositionLocalProvider(
                    LocalAnimationScope provides this,
                    LocalSharedTransitionScope provides this@SharedTransitionLayout
                ) {
                    EditFilterScreen(
                        filterId = 0,
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
                        onDelete = {
                            editingState = EditingState.DELETING
                        }
                    )
                }
            }
        }
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
    ) { entry ->
        CompositionLocalProvider(LocalAnimationScope provides this) {
            EditFilterScreen(
                filterId = entry.arguments?.getLong("filterId")
                    ?: error("no filterId provided"),
                onClose = onNavigateUp
            )
        }
    }
}

@Composable
fun EditFilterScreen(
    filterId: Long,
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
        filterId = filterId,
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

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun EditFilterScreen(
    filterId: Long,
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
    with(LocalSharedTransitionScope.current) {
        Scaffold(
            topBar = {
                AppBar(
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
                    }
                )
            },
        ) { padding ->
            var showDialog: Boolean by remember { mutableStateOf(false) }

            FilterFields(
                state = state,
                contentPadding = padding,
                onUpdateName = onUpdateName,
                onUpdateFilterUrl = onUpdateFilterUrl,
                onUpdateReplaceText = onUpdateReplaceText,
                onUpdateReplaceSubject = onUpdateReplaceSubject,
                onUpdateEncodeUrl = onUpdateEncodeUrl,
                filterNameTextModifier = Modifier.sharedBounds(
                    sharedContentState = rememberSharedContentState(key = "edit-filter-name-$filterId"),
                    animatedVisibilityScope = LocalAnimationScope.current
                ),
                filterUrlTextModifier = Modifier.sharedBounds(
                    sharedContentState = rememberSharedContentState(key = "edit-filter-url-$filterId"),
                    animatedVisibilityScope = LocalAnimationScope.current
                ),
                footerContent = {
                    DeleteButton(
                        enabled = state is SaveFilterState.Editing,
                        onClick = { showDialog = true }
                    )
                }
            )

            if (showDialog && state is SaveFilterState.Editing) {
                ConfirmDeletionDialog(
                    filterName = state.filter.name,
                    onDelete = onDelete,
                    onCancel = { showDialog = false }
                )
            }
        }
    }
}

@Composable
private fun DeleteButton(
    enabled: Boolean,
    onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth()
    ) {
        Button(
            enabled = enabled,
            onClick = onClick
        ) {
            Text(text = stringResource(id = R.string.delete))
        }
    }
}

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
