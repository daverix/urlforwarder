package net.daverix.urlforward

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
class CreateFilterViewModel(
    private val filterDao: FilterDao,
    private val _state: MutableStateFlow<SaveFilterState>
) : ViewModel(), EditableFields by DefaultEditableFields(_state) {
    val state: StateFlow<SaveFilterState> = _state

    @Inject
    constructor(filterDao: FilterDao) : this(filterDao, MutableStateFlow(SaveFilterState.Loading))

    init {
        _state.value = SaveFilterState.Editing(
            filter = createInitialAddFilter(),
            editingState = EditingState.EDITING
        )
    }

    fun save() {
        val state = state.value
        if (state is SaveFilterState.Editing) {
            _state.value = state.copy(editingState = EditingState.SAVING)
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    filterDao.insert(state.filter)
                }
                _state.emit(state.copy(editingState = EditingState.SAVED))
            }
        }
    }
}