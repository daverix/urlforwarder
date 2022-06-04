package net.daverix.urlforward

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.daverix.urlforward.db.FilterDao

class CreateFilterViewModel(
    private val filterDao: FilterDao,
    private val _state: MutableStateFlow<SaveFilterState> = MutableStateFlow(SaveFilterState.Loading)
) : ViewModel(), EditableFields by DefaultEditableFields(_state) {
    val state: StateFlow<SaveFilterState> = _state

    init {
        val created = System.currentTimeMillis()
        _state.value = SaveFilterState.Editing(
            filter = LinkFilter(
                name = "",
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
                _state.emit(SaveFilterState.Saved)
            }
        }
    }
}