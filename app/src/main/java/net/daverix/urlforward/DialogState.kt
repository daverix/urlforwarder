package net.daverix.urlforward

sealed class DialogState {
    object Loading : DialogState()
    data class Filters(val filters: List<LinkFilter>): DialogState()
}
