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
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.daverix.urlforward.dao.LinkFilter
import net.daverix.urlforward.dao.LinkFilterDao
import java.util.*
import javax.inject.Named
import javax.inject.Provider

class UpdateFilterViewModel @AssistedInject constructor(
        @Named("timestamp") private val timestampProvider: Provider<Long>,
        private val filterDao: LinkFilterDao,
        @Named("io") private val ioDispatcher: CoroutineDispatcher,
        @Assisted private val filterId: Long
) : SaveFilterViewModel() {
    override val events = Channel<Event>(Channel.RENDEZVOUS)

    override var title: MutableLiveData<String> = MutableLiveData()
    override var filterUrl: MutableLiveData<String> = MutableLiveData()
    override var replaceText: MutableLiveData<String> = MutableLiveData()
    override var replaceSubject: MutableLiveData<String> = MutableLiveData()
    override var encodeUrl: MutableLiveData<Boolean> = MutableLiveData()
    override var useRegex: MutableLiveData<Boolean> = MutableLiveData()

    private var linkFilter: LinkFilter? = null

    init {
        viewModelScope.launch {
            val filter = withContext(ioDispatcher) {
                filterDao.getFilter(filterId)
            }

            if (filter == null) {
                Log.e("UpdateFilterViewModel", "Filter $filterId does not exist!")
                events.send(Cancel)
                return@launch
            }

            linkFilter = filter
            title.value = filter.title
            filterUrl.value = filter.filterUrl
            replaceText.value = filter.replaceText
            replaceSubject.value = filter.replaceSubject
            encodeUrl.value = !filter.skipEncode
        }
    }

    override fun deleteFilter() {
        val filter = linkFilter
        if (filter != null) {
            viewModelScope.launch {
                val deleted = withContext(ioDispatcher) {
                    filterDao.delete(filter)
                }
                if (deleted > 0) {
                    events.send(Deleted)
                } else {
                    Log.e("UpdateFilterViewModel", "Cannot delete filter $filterId")
                }
            }
        }
    }

    override fun saveFilter() {
        val filter = linkFilter
        if (filter != null) {
            viewModelScope.launch {
                val updatedFilter = filter.copy(
                        title = title.value ?: error("title is null"),
                        filterUrl = filterUrl.value ?: error("filterUrl is null"),
                        replaceText = replaceText.value ?: error("replaceText is null"),
                        replaceSubject = replaceSubject.value ?: error("replaceSubject is null"),
                        updated = timestampProvider.get(),
                        skipEncode = !(encodeUrl.value ?: true)
                )

                val inserted = withContext(ioDispatcher) {
                    filterDao.insertOrUpdate(updatedFilter)
                }
                if (inserted > 0) {
                    events.send(Saved)
                } else {
                    Log.e("UpdateFilterViewModel", "Cannot update filter $filterId")
                }
            }
        }
    }

    override fun cancel() {
        viewModelScope.launch {
            events.send(Cancel)
        }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(filterId: Long): UpdateFilterViewModel
    }
}
