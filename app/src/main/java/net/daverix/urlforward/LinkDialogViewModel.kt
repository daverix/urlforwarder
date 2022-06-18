package net.daverix.urlforward

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import net.daverix.urlforward.db.FilterDao
import javax.inject.Inject

@HiltViewModel
class LinkDialogViewModel @Inject constructor(
    private val urlResolver: UrlResolver,
    private val filterDao: FilterDao
) : ViewModel() {
    private val _state: MutableStateFlow<DialogState> = MutableStateFlow(DialogState.Loading)
    val state: StateFlow<DialogState> = _state

    fun load(url: String, subject: String?) {
        viewModelScope.launch {
            _state.value = DialogState.Filters(filterDao.queryFilters().first().map {
                val urlToOpen = createUrl(it, url, subject)

                LinkDialogListItem(
                    name = it.name,
                    url = urlToOpen,
                    hasMatchingApp = urlResolver.resolveUrl(urlToOpen)
                )
            })
        }
    }
}
