package net.daverix.urlforward

import androidx.compose.runtime.Immutable

@Immutable
data class LinkDialogListItem(
    val name: String,
    val url: String,
    val hasMatchingApp: Boolean
)
