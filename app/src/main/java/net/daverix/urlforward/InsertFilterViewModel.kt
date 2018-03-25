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

import android.annotation.TargetApi
import android.databinding.BaseObservable
import android.os.Build
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
                                                @Named("main") private val mainScheduler: Scheduler,
                                                @Named("modify") private val idleCounter: IdleCounter) : SaveFilterViewModel, BaseObservable() {
    override var title: String by ObservableFieldDelegate("", BR.title)
    override var filterUrl: String by ObservableFieldDelegate("", BR.filterUrl)
    override var replaceText: String by ObservableFieldDelegate("", BR.replaceText)
    override var replaceSubject: String by ObservableFieldDelegate("", BR.replaceSubject)
    override var encodeUrl: Boolean by ObservableFieldDelegate(true, BR.encodeUrl)
    override var useRegex: Boolean by ObservableFieldDelegate(false, BR.useRegex)

    private var saveFilterDisposable: Disposable? = null

    fun loadFilter() {
        filterUrl = "http://example.com/?url=@url&subject=@subject"
        replaceText = "@url"
        replaceSubject = "@subject"
        encodeUrl = true
    }

    fun onDestroy() {
        saveFilterDisposable?.dispose()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    fun restoreInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.apply {
            title = getString("title")
            filterUrl = getString("filterUrl")
            replaceText = getString("filterUrl")
            replaceSubject = getString("replaceSubject")
            encodeUrl = getBoolean("encodeUrl")
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    fun saveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.apply {
            putString("title", title)
            putString("filterUrl", filterUrl)
            putString("replaceText", replaceText)
            putString("replaceSubject", replaceSubject)
            putBoolean("encodeUrl", encodeUrl)
        }
    }

    private fun createFilter() {
        saveFilterDisposable = filterDao.insert(toLinkFilter())
                .doOnSubscribe { idleCounter.increment() }
                .doAfterTerminate { idleCounter.decrement() }
                .subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
                .subscribe(saveFilterCallbacks::onFilterInserted)
    }

    private fun toLinkFilter(): LinkFilter {
        val now = Date(timestampProvider.get())
        return LinkFilter(0,
                title,
                filterUrl,
                replaceText,
                replaceSubject,
                now,
                now,
                !encodeUrl)
    }

    fun cancel() {
        saveFilterCallbacks.onCancelled()
    }

    fun onMenuItemClick(item: MenuItem): Boolean {
        return when {
            item.itemId == R.id.menuSave -> {
                createFilter()
                true
            }
            else -> false
        }
    }
}
