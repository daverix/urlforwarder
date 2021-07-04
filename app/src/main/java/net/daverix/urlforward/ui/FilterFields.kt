package net.daverix.urlforward.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
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
import net.daverix.urlforward.R
import net.daverix.urlforward.SaveFilterState

const val TAG_FILTER_NAME = "filterName"
const val TAG_FILTER_URL = "filterUrl"
const val TAG_REPLACEABLE_TEXT = "replaceableText"
const val TAG_REPLACEABLE_SUBJECT = "replaceableSubject"
const val TAG_ENCODE_URL = "encodeUrl"

@Composable
fun FilterFields(
    state: SaveFilterState.Editing,
    onUpdateName: (String) -> Unit,
    onUpdateFilterUrl: (String) -> Unit,
    onUpdateReplaceText: (String) -> Unit,
    onUpdateReplaceSubject: (String) -> Unit,
    onUpdateEncodeUrl: (Boolean) -> Unit,
    footerContent: @Composable () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        FilterField(
            stringId = R.string.filter_name,
            value = state.filter.name,
            onUpdateValue = onUpdateName,
            textModifier = Modifier.testTag(TAG_FILTER_NAME)
        )
        FilterField(
            stringId = R.string.filter_url,
            value = state.filter.filterUrl,
            onUpdateValue = onUpdateFilterUrl,
            modifier = Modifier.padding(top = 16.dp),
            textModifier = Modifier.testTag(TAG_FILTER_URL)
        )
        FilterField(
            stringId = R.string.replaceable_text,
            value = state.filter.replaceText,
            onUpdateValue = onUpdateReplaceText,
            modifier = Modifier.padding(top = 16.dp),
            textModifier = Modifier.testTag(TAG_REPLACEABLE_TEXT)
        )
        FilterField(
            stringId = R.string.replaceable_subject,
            value = state.filter.replaceSubject,
            onUpdateValue = onUpdateReplaceSubject,
            modifier = Modifier.padding(top = 16.dp),
            textModifier = Modifier.testTag(TAG_REPLACEABLE_SUBJECT)
        )
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
                .padding(top = 16.dp, bottom = 16.dp)
                .requiredHeight(ButtonDefaults.MinHeight)
                .testTag(TAG_ENCODE_URL)
        ) {
            Checkbox(
                checked = state.filter.encoded,
                onCheckedChange = null
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = stringResource(id = R.string.encode_title)
            )
        }

        Text(
            text = stringResource(id = R.string.save_filter_info)
        )

        footerContent()
    }
}

@Composable
private fun FilterField(
    @StringRes stringId: Int,
    value: String,
    onUpdateValue: (String) -> Unit,
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        TextField(
            modifier = textModifier.fillMaxWidth(),
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