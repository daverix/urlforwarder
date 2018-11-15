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

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.daverix.urlforward.dao.LinkFilter
import java.util.*

class LinkFilterViewModel : ViewModel() {
    val id: MutableLiveData<Long> = MutableLiveData()

    val title: MutableLiveData<String> = MutableLiveData()

    val filterUrl: MutableLiveData<String> = MutableLiveData()

    val replaceText: MutableLiveData<String> = MutableLiveData()

    val replaceSubject: MutableLiveData<String> = MutableLiveData()

    val created: MutableLiveData<Date> = MutableLiveData()

    val updated: MutableLiveData<Date> = MutableLiveData()

    val skipEncode: MutableLiveData<Boolean> = MutableLiveData()

    fun update(item: LinkFilter) {
        created.value = item.created
        updated.value = item.updated
        skipEncode.value = item.skipEncode
        filterUrl.value = item.filterUrl
        replaceSubject.value = item.replaceSubject
        replaceText.value = item.replaceText
        title.value = item.title
    }
}
