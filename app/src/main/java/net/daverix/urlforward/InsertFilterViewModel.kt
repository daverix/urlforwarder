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
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.daverix.urlforward.dao.LinkFilter
import net.daverix.urlforward.dao.LinkFilterDao
import java.util.*
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Provider

class InsertFilterViewModel @Inject constructor(
        @Named("timestamp") private val timestampProvider: Provider<Long>,
        @Named("io") private val ioDispatcher: CoroutineDispatcher,
        private val filterDao: LinkFilterDao
) : SaveFilterViewModel() {
    override val events = Channel<Event>(capacity = Channel.RENDEZVOUS)

    override val title: MutableLiveData<String> = MutableLiveData("")
    override val filterUrl: MutableLiveData<String> = MutableLiveData("http://example.com/?url=@url&subject=@subject")
    override val replaceText: MutableLiveData<String> = MutableLiveData("@url")
    override val replaceSubject: MutableLiveData<String> = MutableLiveData("@subject")
    override val encodeUrl: MutableLiveData<Boolean> = MutableLiveData(true)
    override val useRegex: MutableLiveData<Boolean> = MutableLiveData(false)

    override fun saveFilter() {
        viewModelScope.launch {
            val now = timestampProvider.get()
            val filter = LinkFilter(
                    title = title.value ?: error("title is null"),
                    filterUrl = filterUrl.value ?: error("filterUrl is null"),
                    replaceText = replaceText.value ?: error("replaceText is null"),
                    replaceSubject = replaceSubject.value ?: error("replaceSubject is null"),
                    created = now,
                    updated = now,
                    skipEncode = encodeUrl.value ?: error("encodeUrl is null")
            )

            try {
                withContext(ioDispatcher) {
                    filterDao.insertOrUpdate(filter)
                }
                events.send(Saved)
            } catch (ex: Exception) {
                Log.e("IFVM", "Could not save", ex)
            }
        }
    }

    override fun cancel() {
        viewModelScope.launch {
            events.send(Cancel)
        }
    }

    override fun deleteFilter() {
        throw UnsupportedOperationException("this should never be called")
    }

    override fun onCleared() {
        events.cancel()
        super.onCleared()
    }
}
