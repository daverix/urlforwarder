package net.daverix.urlforward.ui

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import net.daverix.urlforward.CreateFilterViewModel
import net.daverix.urlforward.EditingState
import net.daverix.urlforward.R
import net.daverix.urlforward.SaveFilterState

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun PreviewAddFilter(
    @PreviewParameter(SaveFilterStatePreviewParameterProvider::class) state: SaveFilterState
) {
    UrlForwarderTheme {
        SharedTransitionLayout {
            AnimatedVisibility(visible = true) {
                CompositionLocalProvider(
                    LocalAnimationScope provides this,
                    LocalSharedTransitionScope provides this@SharedTransitionLayout
                ) {
                    AddFilterScreen(
                        state = state,
                        onCancel = {},
                        onSave = {},
                        onUpdateEncodeUrl = {},
                        onUpdateReplaceSubject = {},
                        onUpdateReplaceText = {},
                        onUpdateFilterUrl = {},
                        onUpdateName = {}
                    )
                }
            }
        }
    }
}

fun NavController.navigateToAddFilter() {
    navigate("add-filter")
}

fun NavGraphBuilder.addFilterScreen(
    onNavigateUp: () -> Unit
) {
    composable(
        route = "add-filter"
    ) {
        CompositionLocalProvider(LocalAnimationScope provides this) {
            AddFilterScreen(onClose = onNavigateUp)
        }
    }
}

@Composable
fun AddFilterScreen(
    viewModel: CreateFilterViewModel = hiltViewModel(),
    onClose: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val editingState = (state as? SaveFilterState.Editing)?.editingState
    LaunchedEffect(editingState) {
        if(editingState == EditingState.SAVED) {
            onClose()
        }
    }
    AddFilterScreen(
        state = state,
        onSave = viewModel::save,
        onCancel = onClose,
        onUpdateName = viewModel::updateName,
        onUpdateFilterUrl = viewModel::updateFilterUrl,
        onUpdateReplaceText = viewModel::updateReplaceUrl,
        onUpdateReplaceSubject = viewModel::updateReplaceSubject,
        onUpdateEncodeUrl = viewModel::updateEncoded
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun AddFilterScreen(
    state: SaveFilterState,
    onCancel: () -> Unit,
    onSave: () -> Unit,
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
                        Text(text = stringResource(id = R.string.create_filter))
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
                    }
                )
            },
            modifier = Modifier.sharedBounds(
                sharedContentState = rememberSharedContentState(
                    key = "add-filter-bounds"
                ),
                animatedVisibilityScope = LocalAnimationScope.current
            )
        ) { padding ->
            AddFilterContent(
                state = state,
                contentPadding = padding,
                onUpdateName = onUpdateName,
                onUpdateFilterUrl = onUpdateFilterUrl,
                onUpdateReplaceText = onUpdateReplaceText,
                onUpdateReplaceSubject = onUpdateReplaceSubject,
                onUpdateEncodeUrl = onUpdateEncodeUrl
            )
        }
    }
}

@Composable
private fun AddFilterContent(
    state: SaveFilterState,
    contentPadding: PaddingValues,
    onUpdateName: (String) -> Unit,
    onUpdateFilterUrl: (String) -> Unit,
    onUpdateReplaceText: (String) -> Unit,
    onUpdateReplaceSubject: (String) -> Unit,
    onUpdateEncodeUrl: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    when (state) {
        is SaveFilterState.Editing -> FilterFields(
            state = state,
            contentPadding = contentPadding,
            onUpdateName = onUpdateName,
            onUpdateFilterUrl = onUpdateFilterUrl,
            onUpdateReplaceText = onUpdateReplaceText,
            onUpdateReplaceSubject = onUpdateReplaceSubject,
            onUpdateEncodeUrl = onUpdateEncodeUrl
        )
        else -> Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}
