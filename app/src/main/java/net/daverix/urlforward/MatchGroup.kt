package net.daverix.urlforward

import androidx.compose.runtime.Immutable

@Immutable
data class MatchGroup(val name: String, val urlEncode: Boolean)
