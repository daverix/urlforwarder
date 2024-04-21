package net.daverix.urlforward

enum class EditingState {
    EDITING,
    SAVING,
    SAVED,
    DELETING,
    DELETED
}
sealed class SaveFilterState {
    data object Loading : SaveFilterState()

    data class Editing(val filter: LinkFilter, val editingState: EditingState) : SaveFilterState()
}
