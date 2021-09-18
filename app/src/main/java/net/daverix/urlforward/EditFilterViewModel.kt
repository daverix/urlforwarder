package net.daverix.urlforward

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.daverix.urlforward.db.FilterDao

class EditFilterViewModel(
    private val filterDao: FilterDao,
    private val filterId: Long,
    private val _state: MutableStateFlow<SaveFilterState> = MutableStateFlow(SaveFilterState.Loading)
) : ViewModel(), EditableFields by DefaultEditableFields(_state) {
    val state: StateFlow<SaveFilterState> = _state

    init {
        viewModelScope.launch {
            val filter = filterDao.queryFilter(filterId)
            if(filter != null) {
                _state.value = SaveFilterState.Editing(
                    filter = filter
                )
            }
        }
    }

    fun save() {
        viewModelScope.launch {
            val currentState = state.value
            if(currentState is SaveFilterState.Editing) {
                _state.value = SaveFilterState.Loading
                viewModelScope.launch {
                    withContext(Dispatchers.IO) {
                        filterDao.update(currentState.filter)
                    }
                    _state.value = SaveFilterState.Closing
                }
            }
        }
    }

    fun delete() {
        val currentState = state.value
        if(currentState is SaveFilterState.Editing) {
            _state.value = SaveFilterState.Loading
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    filterDao.delete(currentState.filter.id)
                }
                _state.value = SaveFilterState.Closing
            }
        }
    }
}
