/*
    UrlForwarder makes it possible to use bookmarklets on Android
    Copyright (C) 2018 David Laurell

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.daverix.urlforward


import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import net.daverix.urlforward.dao.LinkFilter
import net.daverix.urlforward.dao.LinkFilterDao
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Named

class FiltersViewModel @Inject constructor(
        linkFilterDao: LinkFilterDao
) : ViewModel() {
    val events = Channel<Event>(capacity = Channel.RENDEZVOUS)

    private val filtersState = MutableLiveData<State>(State.Loading)

    init {
        viewModelScope.launch {
            try {
                linkFilterDao.getFilters()
                        .collect { items ->
                            filtersState.value = State.Loaded(items)
                        }
            } catch (ex: Exception) {
                filtersState.value = State.Error
                Log.e("FiltersViewModel", "Cannot load filters", ex)
            }
        }
    }

    val nothingAdded: LiveData<Boolean> = filtersState.map { it is State.Loaded && it.items.isEmpty() }

    val loading: LiveData<Boolean> = filtersState.map { it is State.Loading }

    val filters: LiveData<List<FilterRowViewModel>> = filtersState.map { state ->
        if (state is State.Loaded) {
            state.items.map {
                FilterRowViewModel(it.title, it.filterUrl, it.id) { id ->
                    viewModelScope.launch {
                        events.send(EditFilter(id))
                    }
                }
            }
        } else emptyList()
    }

    fun addFilter() {
        viewModelScope.launch {
            events.send(CreateFilter)
        }
    }

    sealed class State {
        object Loading : State()
        class Loaded(val items: List<LinkFilter>) : State()
        object Error : State()
    }
}
