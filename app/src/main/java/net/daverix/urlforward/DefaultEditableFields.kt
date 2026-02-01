package net.daverix.urlforward

import kotlinx.coroutines.flow.MutableStateFlow

class DefaultEditableFields(
    private val state: MutableStateFlow<SaveFilterState>
) : EditableFields {
    override fun updateName(name: String) {
        updateFilter { copy(name = name) }
    }

    override fun updateFilterUrl(url: String) {
        updateFilter { copy(filterUrl = url) }
    }

    override fun updateReplaceUrl(url: String) {
        updateFilter { copy(replaceText = url) }
    }

    override fun updateReplaceSubject(subject: String) {
        updateFilter { copy(replaceSubject = subject) }
    }

    override fun updateEncoded(encoded: Boolean) {
        updateFilter { copy(encoded = encoded) }
    }

    override fun updateTextPattern(pattern: String) {
        updateFilter { copy(textPattern = pattern) }
    }

    override fun updateSubjectPattern(pattern: String) {
        updateFilter { copy(subjectPattern = pattern) }
    }

    private fun updateFilter(func: LinkFilter.() -> LinkFilter) {
        val currentState = state.value
        if(currentState is SaveFilterState.Editing) {
            state.value = currentState.copy(
                filter = func(currentState.filter)
            )
        }
    }
}