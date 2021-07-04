package net.daverix.urlforward.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import net.daverix.urlforward.CreateFilterViewModel
import net.daverix.urlforward.R
import net.daverix.urlforward.SaveFilterState


@Composable
fun AddFilterScreen(
    viewModel: CreateFilterViewModel,
    onCancel: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    AddFilterScreen(
        state = state,
        onSave = viewModel::save,
        onCancel = onCancel,
        onUpdateName = viewModel::updateTitle,
        onUpdateFilterUrl = viewModel::updateFilterUrl,
        onUpdateReplaceText = viewModel::updateReplaceUrl,
        onUpdateReplaceSubject = viewModel::updateReplaceSubject,
        onUpdateEncodeUrl = viewModel::updateEncoded
    )
}

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
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
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
            },
            elevation = 8.dp
        )

        Surface {
            AddFilterContent(
                state = state,
                modifier = Modifier.weight(1f),
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
    onUpdateName: (String) -> Unit,
    onUpdateFilterUrl: (String) -> Unit,
    onUpdateReplaceText: (String) -> Unit,
    onUpdateReplaceSubject: (String) -> Unit,
    onUpdateEncodeUrl: (Boolean) -> Unit,
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
                onUpdateEncodeUrl = onUpdateEncodeUrl
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

@Preview
@Composable
private fun PreviewAddFilter() {
    UrlForwarderTheme(darkTheme = false) {
        AddFilterScreen(
            state = SaveFilterState.Loading,
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

@Preview
@Composable
private fun PreviewAddFilterDark() {
    UrlForwarderTheme(darkTheme = true) {
        AddFilterScreen(
            state = SaveFilterState.Loading,
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
