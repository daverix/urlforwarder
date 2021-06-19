package net.daverix.urlforward

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.daverix.urlforward.db.FilterDao

class CreateFilterViewModel(
    private val filterDao: FilterDao,
    private val _state: MutableStateFlow<SaveFilterState> = MutableStateFlow(SaveFilterState.Loading)
) : ViewModel(), EditableFields by DefaultEditableFields(_state) {
    private val _actions = MutableSharedFlow<SaveFilterAction>()
    val actions: Flow<SaveFilterAction> = _actions
    val state: StateFlow<SaveFilterState> = _state

    init {
        val created = System.currentTimeMillis()
        _state.value = SaveFilterState.Editing(
            filter = LinkFilter(
                title = "",
                filterUrl = "http://example.com/?url=@url&subject=@subject",
                replaceText = "@url",
                replaceSubject = "@subject",
                created = created,
                updated = created,
                encoded = true
            )
        )
    }

    fun save() {
        val state = state.value
        if(state is SaveFilterState.Editing) {
            _state.value = SaveFilterState.Loading
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    filterDao.insert(state.filter)
                }
                _actions.emit(SaveFilterAction.CloseSuccessfully)
            }
        }
    }
}