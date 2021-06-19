package net.daverix.urlforward

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import net.daverix.urlforward.db.FilterDao

class LinkDialogViewModel(
    private val filterDao: FilterDao
) : ViewModel() {
    private val _state: MutableStateFlow<DialogState> = MutableStateFlow(DialogState.Loading)
    val state: StateFlow<DialogState> = _state

    init {
        viewModelScope.launch {
            _state.value = DialogState.Filters(
                filters = filterDao.queryFilters().first()
            )
        }
    }
}
