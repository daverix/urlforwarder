package net.daverix.urlforward

import kotlinx.coroutines.flow.MutableStateFlow

class DefaultEditableFields(
    private val state: MutableStateFlow<SaveFilterState>
) : EditableFields {
    override fun updateName(name: String) {
        val currentState = state.value
        if(currentState is SaveFilterState.Editing) {
            state.value = currentState.copy(
                filter = currentState.filter.copy(
                    name = name
                )
            )
        }
    }

    override fun updateRegex(regexPattern: String) {
        val currentState = state.value
        if (currentState is SaveFilterState.Editing) {
            state.value = currentState.copy(
                filter = currentState.filter.copy(
                    regexPattern = regexPattern
                )
            )
        }
    }

    override fun updateFilterUrl(url: String) {
        val currentState = state.value
        if(currentState is SaveFilterState.Editing) {
            state.value = currentState.copy(
                filter = currentState.filter.copy(
                    filterUrl = url
                )
            )
        }
    }

    override fun updateReplaceUrl(url: String) {
        val currentState = state.value
        if(currentState is SaveFilterState.Editing) {
            state.value = currentState.copy(
                filter = currentState.filter.copy(
                    replaceText = url
                )
            )
        }
    }

    override fun updateReplaceSubject(subject: String) {
        val currentState = state.value
        if(currentState is SaveFilterState.Editing) {
            state.value = currentState.copy(
                filter = currentState.filter.copy(
                    replaceSubject = subject
                )
            )
        }
    }

    override fun updateEncoded(encoded: Boolean) {
        val currentState = state.value
        if(currentState is SaveFilterState.Editing) {
            state.value = currentState.copy(
                filter = currentState.filter.copy(
                    encoded = encoded
                )
            )
        }
    }
}