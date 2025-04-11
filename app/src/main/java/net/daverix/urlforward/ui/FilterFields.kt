package net.daverix.urlforward.ui

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

@Preview(showBackground = true)
@Composable
private fun FilterFieldsPreview() {
    UrlForwarderTheme {
        FilterFields(
            state = SaveFilterState.Editing(
                filter = LinkFilter(
                    id = 1,
                    name = "Preview",
                    filterUrl = "http://someurl.com?url=@text",
                    replaceText = "@text",
                    replaceSubject = "@subject",
                    created = 0L,
                    updated = 0L,
                    encoded = false
                ),
                editingState = EditingState.EDITING
            ),
            contentPadding = PaddingValues(),
            onUpdateName = { },
            onUpdateFilterUrl = { },
            onUpdateReplaceText = { },
            onUpdateReplaceSubject = { },
            onUpdateEncodeUrl = { }
        )
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
        FilterField(
            stringId = R.string.output_url,
            value = (state as? SaveFilterState.Editing)?.filter?.filterUrl ?: "",
            enabled = (state as? SaveFilterState.Editing)?.editingState == EditingState.EDITING,
            onUpdateValue = onUpdateFilterUrl,
            modifier = filterUrlTextModifier.padding(horizontal = horizontalPadding),
            textFieldModifier = Modifier.testTag(TAG_FILTER_URL),
        )

        Spacer(modifier = Modifier.height(16.dp))
        FilterField(
            stringId = R.string.replaceable_text,
            value = (state as? SaveFilterState.Editing)?.filter?.replaceText ?: "",
            enabled = (state as? SaveFilterState.Editing)?.editingState == EditingState.EDITING,
            onUpdateValue = onUpdateReplaceText,
            modifier = Modifier.padding(horizontal = horizontalPadding),
            textFieldModifier = Modifier.testTag(TAG_REPLACEABLE_TEXT)
        )

        Spacer(modifier = Modifier.height(16.dp))
        FilterField(
            stringId = R.string.replaceable_subject,
            value = (state as? SaveFilterState.Editing)?.filter?.replaceSubject ?: "",
            enabled = (state as? SaveFilterState.Editing)?.editingState == EditingState.EDITING,
            onUpdateValue = onUpdateReplaceSubject,
            modifier = Modifier.padding(horizontal = horizontalPadding),
            textFieldModifier = Modifier.testTag(TAG_REPLACEABLE_SUBJECT)
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
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = stringResource(id = R.string.encode_title)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(id = R.string.save_filter_info),
            modifier = Modifier.padding(horizontal = 16.dp)
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
    @StringRes stringId: Int,
    value: String,
    enabled: Boolean,
    onUpdateValue: (String) -> Unit,
    modifier: Modifier = Modifier,
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
            label = {
                Text(
                    text = stringResource(id = stringId),
                    fontWeight = FontWeight.Bold,
                    modifier = textModifier
                )
            },
        )
    }
}