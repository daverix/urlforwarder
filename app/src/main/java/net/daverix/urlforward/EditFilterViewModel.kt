package net.daverix.urlforward

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.daverix.urlforward.db.FilterDao
import javax.inject.Inject

@HiltViewModel
class EditFilterViewModel(
    private val filterDao: FilterDao,
    private val savedStateHandle: SavedStateHandle,
    private val _state: MutableStateFlow<SaveFilterState>
) : ViewModel(), EditableFields by DefaultEditableFields(_state) {
    val state: StateFlow<SaveFilterState> = _state

    @Inject
    constructor(filterDao: FilterDao, savedStateHandle: SavedStateHandle) : this(
        filterDao,
        savedStateHandle,
        MutableStateFlow(SaveFilterState.Loading)
    )

    init {
        viewModelScope.launch {
            val filterId = savedStateHandle.get<Long>("filterId")
                ?: error("filterId not set")

            val filter = filterDao.queryFilter(filterId)
            if (filter != null) {
                _state.value = SaveFilterState.Editing(
                    filter = filter,
                    editingState = EditingState.EDITING
                )
            }
        }
    }

    fun save() {
        viewModelScope.launch {
            val currentState = state.value
            if (currentState is SaveFilterState.Editing) {
                _state.value = currentState.copy(editingState = EditingState.SAVING)
                viewModelScope.launch {
                    withContext(Dispatchers.IO) {
                        filterDao.update(currentState.filter)
                    }
                    _state.emit(currentState.copy(editingState = EditingState.SAVED))
                }
            }
        }
    }

    fun delete() {
        val currentState = state.value
        if (currentState is SaveFilterState.Editing) {
            _state.value = currentState.copy(editingState = EditingState.DELETING)
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    filterDao.delete(currentState.filter.id)
                }
                _state.emit(currentState.copy(editingState = EditingState.DELETED))
            }
        }
    }
}
