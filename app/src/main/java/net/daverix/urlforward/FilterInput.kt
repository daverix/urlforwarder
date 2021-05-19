package net.daverix.urlforward

import androidx.compose.runtime.Immutable

@Immutable
data class FilterInput(
    val pattern: String,
    val patternIr: RegexIR,
    val groups: List<MatchGroup>,
    val visibleGroupCount: Int
)
