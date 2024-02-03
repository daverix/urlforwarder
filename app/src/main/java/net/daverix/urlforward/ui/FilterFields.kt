package net.daverix.urlforward.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import net.daverix.urlforward.EditingState
import net.daverix.urlforward.R
import net.daverix.urlforward.SaveFilterState

const val TAG_FILTER_NAME = "filterName"
const val TAG_FILTER_URL = "filterUrl"
const val TAG_REGEX_PATTERN = "regexPattern"
const val TAG_REPLACEABLE_TEXT = "replaceableText"
const val TAG_REPLACEABLE_SUBJECT = "replaceableSubject"
const val TAG_ENCODE_URL = "encodeUrl"

@Composable
fun FilterFields(
    state: SaveFilterState.Editing,
    contentPadding: PaddingValues,
    onUpdateName: (String) -> Unit,
    onUpdateRegex: (String) -> Unit,
    onUpdateFilterUrl: (String) -> Unit,
    onUpdateReplaceText: (String) -> Unit,
    onUpdateReplaceSubject: (String) -> Unit,
    onUpdateEncodeUrl: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
    footerContent: @Composable (() -> Unit)? = null
) {
    val horizontalPadding = 16.dp
    Column(
        modifier = modifier
            .padding(contentPadding)
            .verticalScroll(scrollState)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        FilterField(
            stringId = R.string.filter_name,
            value = state.filter.name,
            enabled = state.editingState == EditingState.EDITING,
            onUpdateValue = onUpdateName,
            textModifier = Modifier.testTag(TAG_FILTER_NAME),
            modifier = Modifier.padding(horizontal = horizontalPadding)
        )

        Spacer(modifier = Modifier.height(16.dp))
        FilterField(
            stringId = R.string.regex_pattern,
            value = state.filter.regexPattern,
            enabled = state.editingState == EditingState.EDITING,
            onUpdateValue = onUpdateRegex,
            modifier = Modifier.padding(horizontal = horizontalPadding),
            textModifier = Modifier.testTag(TAG_REGEX_PATTERN)
        )

        Spacer(modifier = Modifier.height(16.dp))
        FilterField(
            stringId = R.string.filter_url,
            value = state.filter.filterUrl,
            enabled = state.editingState == EditingState.EDITING,
            onUpdateValue = onUpdateFilterUrl,
            modifier = Modifier.padding(horizontal = horizontalPadding),
            textModifier = Modifier.testTag(TAG_FILTER_URL)
        )

        Spacer(modifier = Modifier.height(16.dp))
        FilterField(
            stringId = R.string.replaceable_text,
            value = state.filter.replaceText,
            enabled = state.editingState == EditingState.EDITING,
            onUpdateValue = onUpdateReplaceText,
            modifier = Modifier.padding(horizontal = horizontalPadding),
            textModifier = Modifier.testTag(TAG_REPLACEABLE_TEXT)
        )

        Spacer(modifier = Modifier.height(16.dp))
        FilterField(
            stringId = R.string.replaceable_subject,
            value = state.filter.replaceSubject,
            enabled = state.editingState == EditingState.EDITING,
            onUpdateValue = onUpdateReplaceSubject,
            modifier = Modifier.padding(horizontal = horizontalPadding),
            textModifier = Modifier.testTag(TAG_REPLACEABLE_SUBJECT)
        )

        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(MaterialTheme.shapes.small)
                .toggleable(
                    indication = rememberRipple(color = MaterialTheme.colors.primary),
                    value = state.filter.encoded,
                    role = Role.Checkbox,
                    interactionSource = remember { MutableInteractionSource() },
                    onValueChange = { onUpdateEncodeUrl(it) }
                )
                .padding(horizontal = horizontalPadding, vertical = 8.dp)
                .requiredHeight(ButtonDefaults.MinHeight)
                .testTag(TAG_ENCODE_URL)
        ) {
            Checkbox(
                enabled = state.editingState == EditingState.EDITING,
                checked = state.filter.encoded,
                onCheckedChange = null
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = stringResource(id = R.string.encode_title)
            )
        }

        footerContent?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                footerContent()
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(id = R.string.save_filter_info),
            modifier = Modifier.padding(horizontal = 16.dp)
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
    textModifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        TextField(
            modifier = textModifier.fillMaxWidth(),
            enabled = enabled,
            value = value,
            onValueChange = onUpdateValue,
            singleLine = true,
            label = {
                Text(
                    text = stringResource(id = stringId),
                    fontWeight = FontWeight.Bold
                )
            }
        )
    }
}