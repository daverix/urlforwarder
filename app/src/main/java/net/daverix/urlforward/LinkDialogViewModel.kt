package net.daverix.urlforward

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import net.daverix.urlforward.db.FilterDao

class LinkDialogViewModel(
    private val urlResolver: UrlResolver,
    private val filterDao: FilterDao,
    private val url: String,
    private val subject: String?
) : ViewModel() {
    private val _state: MutableStateFlow<DialogState> = MutableStateFlow(DialogState.Loading)
    val state: StateFlow<DialogState> = _state

    init {
        viewModelScope.launch {
            _state.value = DialogState.Filters(filterDao.queryFilters().first().map(::createItem))
        }
    }

    private fun createItem(filter: LinkFilter): LinkDialogListItem {
        val urlToOpen = createUrl(filter, url, subject)

        return LinkDialogListItem(
            name = filter.name,
            url = urlToOpen,
            hasMatchingApp = urlResolver.resolveUrl(urlToOpen)
        )
    }
}
