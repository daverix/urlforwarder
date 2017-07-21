/*
    UrlForwarder makes it possible to use bookmarklets on Android
    Copyright (C) 2017 David Laurell

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

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.os.Bundle
import android.view.MenuItem
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import net.daverix.urlforward.dao.LinkFilter
import net.daverix.urlforward.dao.LinkFilterDao
import net.daverix.urlforward.dao.insert
import java.util.*
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Provider

class InsertFilterViewModel @Inject constructor(@Named("timestamp") private val timestampProvider: Provider<Long>,
                                                private val filterDao: LinkFilterDao,
                                                private val saveFilterCallbacks: InsertFilterCallbacks,
                                                @Named("io") private val ioScheduler: Scheduler,
                                                @Named("main") private val mainScheduler: Scheduler) : SaveFilterViewModel {
    override var title: ObservableField<String> = ObservableField("")
    override var filterUrl: ObservableField<String> = ObservableField("")
    override var replaceText: ObservableField<String> = ObservableField("")
    override var replaceSubject: ObservableField<String> = ObservableField("")
    override var encodeUrl: ObservableBoolean = ObservableBoolean(true)

    private var saveFilterDisposable: Disposable? = null

    fun loadFilter() {
        filterUrl.set("http://example.com/?url=@url&subject=@subject")
        replaceText.set("@url")
        replaceSubject.set("@subject")
        encodeUrl.set(true)
    }

    fun onDestroy() {
        saveFilterDisposable?.dispose()
    }

    fun restoreInstanceState(savedInstanceState: Bundle) {
        title.set(savedInstanceState.getString("title"))
        filterUrl.set(savedInstanceState.getString("filterUrl"))
        replaceText.set(savedInstanceState.getString("filterUrl"))
        replaceSubject.set(savedInstanceState.getString("replaceSubject"))
        encodeUrl.set(savedInstanceState.getBoolean("encodeUrl"))
    }

    fun saveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putString("title", title.get())
        savedInstanceState.putString("filterUrl", filterUrl.get())
        savedInstanceState.putString("replaceText", replaceText.get())
        savedInstanceState.putString("replaceSubject", replaceSubject.get())
        savedInstanceState.putBoolean("encodeUrl", encodeUrl.get())
    }

    fun createFilter() {
        saveFilterDisposable = filterDao.insert(toLinkFilter())
                .subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
                .subscribe(saveFilterCallbacks::onFilterInserted)
    }

    private fun toLinkFilter(): LinkFilter {
        val now = Date(timestampProvider.get())
        return LinkFilter(0,
                title.get(),
                filterUrl.get(),
                replaceText.get(),
                replaceSubject.get(),
                now,
                now,
                !encodeUrl.get())
    }

    fun cancel() {
        saveFilterCallbacks.onCancelled()
    }

    fun onMenuItemClick(item: MenuItem): Boolean {
        when {
            item.itemId == R.id.menuSave -> {
                createFilter()
                return true
            }
            else -> return false
        }
    }
}
