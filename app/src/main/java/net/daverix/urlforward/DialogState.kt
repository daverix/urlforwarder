package net.daverix.urlforward

sealed class DialogState {
    data object Loading : DialogState()
    data class Filters(val filters: List<LinkDialogListItem>): DialogState()
}
