package net.daverix.urlforward

sealed class SaveFilterState {
    object Loading : SaveFilterState()

    data class Editing(val filter: LinkFilter) : SaveFilterState()

    object Closing : SaveFilterState()
}