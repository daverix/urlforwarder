package net.daverix.urlforward.ui

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.map
import net.daverix.urlforward.AddFilterViewModel
import net.daverix.urlforward.CreateUrlResult
import net.daverix.urlforward.FilterInput
import net.daverix.urlforward.MatchGroup


@Composable
fun AddFilterScreen(viewModel: AddFilterViewModel) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Text(text = "New filter")
            },
            navigationIcon = {
                IconButton(onClick = viewModel::cancel) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Cancel"
                    )
                }
            },
            actions = {
                TextButton(
                    onClick = viewModel::saveFilter
                ) {
                    Text(
                        text = "Save",
                        color = MaterialTheme.colors.onPrimary
                    )
                }
            },
            elevation = 8.dp
        )

        val scrollState = rememberScrollState()

        Column(modifier = Modifier.verticalScroll(scrollState)) {
            Column(modifier = Modifier.padding(all = 16.dp)) {
                Text(
                    text = "Name",
                    fontWeight = Bold
                )
                val name by viewModel.name.collectAsState()
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = name,
                    onValueChange = viewModel::updateName,
                    singleLine = true
                )
            }

            Divider()

            val urlFilter by viewModel.urlFilter.collectAsState()
            FilterInput(
                title = "Input Url",
                intentAction = Intent.EXTRA_TEXT,
                filterInput = urlFilter,
                onChangePattern = viewModel::updateUrlPattern,
                onChangeGroupName = viewModel::updateUrlGroupName,
                onChangeGroupUrlEncode = viewModel::updateUrlGroupUrlEncode
            )

            Divider()

            val subjectFilter by viewModel.subjectFilter.collectAsState()
            FilterInput(
                title = "Input Subject",
                intentAction = Intent.EXTRA_SUBJECT,
                filterInput = subjectFilter,
                onChangePattern = viewModel::updateSubjectPattern,
                onChangeGroupName = viewModel::updateSubjectGroupName,
                onChangeGroupUrlEncode = viewModel::updateSubjectGroupUrlEncode
            )

            Divider()

            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Output url", fontWeight = Bold)

                val outputUrl by viewModel.outputUrl.collectAsState()
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = outputUrl,
                    onValueChange = viewModel::updateOutputUrl,
                    singleLine = true
                )
            }

            Divider(modifier = Modifier.padding(top = 8.dp, bottom = 8.dp))

            Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp)) {
                Text(text = "Test", fontWeight = Bold)

                val text by viewModel.testOutput
                    .map {
                        when (it) {
                            is CreateUrlResult.Successful -> it.url
                            is CreateUrlResult.NoMatch -> "No match"
                            is CreateUrlResult.SyntaxError -> "Syntax error"
                        }
                    }
                    .collectAsState("")

                Text(
                    text = text,
                    modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Test url")
                val testUrl by viewModel.testUrl.collectAsState()
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = testUrl,
                    onValueChange = viewModel::updateTestUrl,
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Test subject")
                val testSubject by viewModel.testSubject.collectAsState()
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = testSubject,
                    onValueChange = viewModel::updateTestSubject,
                    singleLine = true
                )
            }
        }
    }
}

@Composable
private fun FilterInput(
    title: String,
    intentAction: String,
    filterInput: FilterInput,
    onChangePattern: (String) -> Unit,
    onChangeGroupName: (Int, String) -> Unit,
    onChangeGroupUrlEncode: (Int, Boolean) -> Unit
) {
    Column(modifier = Modifier.padding(all = 16.dp)) {
        Text(text = "$title ($intentAction)", fontWeight = Bold)
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Matcher")
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = filterInput.pattern,
            onValueChange = {
                onChangePattern(it)
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                autoCorrect = false,
                imeAction = ImeAction.Next
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Groups")
        filterInput.groups.forEachIndexed { index, group ->
            GroupInput(
                index = index,
                group = group,
                onChangeGroupName = onChangeGroupName,
                onChangeGroupUrlEncode = onChangeGroupUrlEncode
            )
        }
    }
}

@Composable
private fun GroupInput(
    index: Int,
    group: MatchGroup,
    onChangeGroupName: (Int, String) -> Unit,
    onChangeGroupUrlEncode: (Int, Boolean) -> Unit
) {
    Row(
        modifier = Modifier.padding(bottom = 8.dp, top = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$index",
            fontSize = 20.sp,
            textAlign = TextAlign.End,
            modifier = Modifier
                .width(56.dp)
                .padding(end = 8.dp)
        )
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = group.name,
            onValueChange = {
                onChangeGroupName(index, it)
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                autoCorrect = false,
                imeAction = ImeAction.Next
            )
        )
    }
    Row(modifier = Modifier.padding(bottom = 8.dp, top = 8.dp)) {
        Checkbox(
            checked = group.urlEncode,
            onCheckedChange = {
                onChangeGroupUrlEncode(index, it)
            },
            modifier = Modifier.padding(start = 56.dp, end = 8.dp)
        )
        Text(
            text = "Url encode"
        )
    }

}

@Preview
@Composable
private fun PreviewAddFilter() {
    val viewModel = previewViewModel()

    UrlForwarderTheme(darkTheme = false) {
        Surface(color = MaterialTheme.colors.background) {
            AddFilterScreen(viewModel)
        }
    }
}

@Preview
@Composable
private fun PreviewAddFilterDark() {
    val viewModel = previewViewModel()

    UrlForwarderTheme(darkTheme = true) {
        Surface(color = MaterialTheme.colors.background) {
            AddFilterScreen(viewModel)
        }
    }
}

private fun previewViewModel() = AddFilterViewModel().apply {

}
