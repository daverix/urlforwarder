package net.daverix.urlforward.ui

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.annotation.StringRes
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.daverix.urlforward.EditingState
import net.daverix.urlforward.LinkFilter
import net.daverix.urlforward.R
import net.daverix.urlforward.SaveFilterState

const val TAG_FILTER_NAME = "filterName"
const val TAG_FILTER_URL = "filterUrl"
const val TAG_REPLACEABLE_TEXT = "replaceableText"
const val TAG_REPLACEABLE_SUBJECT = "replaceableSubject"
const val TAG_ENCODE_URL = "encodeUrl"

const val TAG_TEXT_PATTERN = "textPattern"
const val TAG_SUBJECT_PATTERN = "subjectPattern"

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun FilterFieldsPreview() {
    UrlForwarderTheme {
        Surface {
            FilterFields(
                state = SaveFilterState.Editing(
                    filter = LinkFilter(
                        id = 1,
                        name = "Preview",
                        filterUrl = "http://someurl.com?url=@text",
                        replaceText = "@text1",
                        replaceSubject = "@subject",
                        created = 0L,
                        updated = 0L,
                        encoded = false,
                        textPattern = "https://myawesomefilter.com/(.*)",
                        subjectPattern = ".*"
                    ),
                    editingState = EditingState.EDITING
                ),
                contentPadding = PaddingValues(),
                onUpdateName = { },
                onUpdateFilterUrl = { },
                onUpdateReplaceText = { },
                onUpdateReplaceSubject = { },
                onUpdateEncodeUrl = { },
                onUpdateTextPattern = { },
                onUpdateSubjectPattern = { },
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalSharedTransitionApi::class)
@Composable
fun FilterFields(
    state: SaveFilterState,
    contentPadding: PaddingValues,
    onUpdateName: (String) -> Unit,
    onUpdateFilterUrl: (String) -> Unit,
    onUpdateReplaceText: (String) -> Unit,
    onUpdateReplaceSubject: (String) -> Unit,
    onUpdateEncodeUrl: (Boolean) -> Unit,
    onUpdateTextPattern: (String) -> Unit,
    onUpdateSubjectPattern: (String) -> Unit,
    modifier: Modifier = Modifier,
    filterNameTextModifier: Modifier = Modifier,
    filterUrlTextModifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
    footerContent: @Composable (() -> Unit)? = null
) {
    val horizontalPadding = 16.dp

    Column(
        modifier = modifier
            .padding(contentPadding)
            .consumeWindowInsets(
                WindowInsets.displayCutout.only(WindowInsetsSides.Vertical)
            )
            .displayCutoutPadding()
            .verticalScroll(scrollState)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        FilterField(
            stringId = R.string.filter_name,
            value = (state as? SaveFilterState.Editing)?.filter?.name ?: "",
            enabled = (state as? SaveFilterState.Editing)?.editingState == EditingState.EDITING,
            onUpdateValue = onUpdateName,
            textFieldModifier = filterNameTextModifier.testTag(TAG_FILTER_NAME),
            modifier = Modifier.padding(horizontal = horizontalPadding),
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.title_incoming_text),
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.subtitle1
        )
        Text(
            text = stringResource(R.string.description_incoming_text),
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.caption
        )

        Spacer(modifier = Modifier.height(8.dp))
        FilterField(
            stringId = R.string.title_pattern,
            value = (state as? SaveFilterState.Editing)?.filter?.textPattern ?: "",
            enabled = (state as? SaveFilterState.Editing)?.editingState == EditingState.EDITING,
            onUpdateValue = onUpdateTextPattern,
            modifier = Modifier.padding(horizontal = horizontalPadding),
            textFieldModifier = Modifier.testTag(TAG_TEXT_PATTERN)
        )

        Spacer(modifier = Modifier.height(16.dp))
        // output name
        FilterField(
            stringId = R.string.title_output_name,
            value = (state as? SaveFilterState.Editing)?.filter?.replaceText ?: "",
            enabled = (state as? SaveFilterState.Editing)?.editingState == EditingState.EDITING,
            onUpdateValue = onUpdateReplaceText,
            modifier = Modifier.padding(horizontal = horizontalPadding),
            textFieldModifier = Modifier.testTag(TAG_REPLACEABLE_TEXT)
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.title_incoming_subject),
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.subtitle1
        )
        Text(
            text = stringResource(R.string.description_incoming_subject),
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.caption
        )

        Spacer(modifier = Modifier.height(8.dp))
        FilterField(
            stringId = R.string.title_pattern,
            value = (state as? SaveFilterState.Editing)?.filter?.subjectPattern ?: "",
            enabled = (state as? SaveFilterState.Editing)?.editingState == EditingState.EDITING,
            onUpdateValue = onUpdateSubjectPattern,
            modifier = Modifier.padding(horizontal = horizontalPadding),
            textFieldModifier = Modifier.testTag(TAG_SUBJECT_PATTERN)
        )

        Spacer(modifier = Modifier.height(16.dp))
        // output name
        FilterField(
            stringId = R.string.title_output_name,
            value = (state as? SaveFilterState.Editing)?.filter?.replaceSubject ?: "",
            enabled = (state as? SaveFilterState.Editing)?.editingState == EditingState.EDITING,
            onUpdateValue = onUpdateReplaceSubject,
            modifier = Modifier.padding(horizontal = horizontalPadding),
            textFieldModifier = Modifier.testTag(TAG_REPLACEABLE_SUBJECT)
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.title_outgoing_url),
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.subtitle1
        )

        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .toggleable(
                    enabled = (state as? SaveFilterState.Editing)?.editingState == EditingState.EDITING,
                    indication = ripple(color = MaterialTheme.colors.primary),
                    value = (state as? SaveFilterState.Editing)?.filter?.encoded == true,
                    role = Role.Checkbox,
                    interactionSource = remember { MutableInteractionSource() },
                    onValueChange = { onUpdateEncodeUrl(it) }
                )
                .padding(horizontal = horizontalPadding, vertical = 8.dp)
                .testTag(TAG_ENCODE_URL)
        ) {
            Checkbox(
                enabled = (state as? SaveFilterState.Editing)?.editingState == EditingState.EDITING,
                checked = (state as? SaveFilterState.Editing)?.filter?.encoded == true,
                onCheckedChange = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(id = R.string.encode_title),
                style = MaterialTheme.typography.button
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        FilterField(
            value = (state as? SaveFilterState.Editing)?.filter?.filterUrl ?: "",
            enabled = (state as? SaveFilterState.Editing)?.editingState == EditingState.EDITING,
            onUpdateValue = onUpdateFilterUrl,
            modifier = filterUrlTextModifier.padding(horizontal = horizontalPadding),
            textFieldModifier = Modifier.testTag(TAG_FILTER_URL),
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(id = R.string.description_outgoing_url),
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.body1
        )

        footerContent?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                footerContent()
            }
        }
        Spacer(
            modifier = Modifier
                .padding(top = 16.dp)
                .windowInsetsBottomHeight(
                    if (WindowInsets.isImeVisible)
                        WindowInsets.ime
                    else
                        WindowInsets.systemBars
                )
        )
    }
}

@Composable
private fun FilterField(
    value: String,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    @StringRes stringId: Int? = null,
    onUpdateValue: (String) -> Unit,
    textFieldModifier: Modifier = Modifier,
    textModifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        TextField(
            modifier = textFieldModifier.fillMaxWidth(),
            enabled = enabled,
            value = value,
            onValueChange = onUpdateValue,
            singleLine = true,
            label = stringId?.let { localStringId ->
                {
                    Text(
                        text = stringResource(id = localStringId),
                        fontWeight = FontWeight.Bold,
                        modifier = textModifier
                    )
                }
            },
        )
    }
}