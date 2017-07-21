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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import net.daverix.urlforward.dao.*
import java.util.*
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Provider


class UpdateFilterViewModel @Inject constructor(@Named("timestamp") private val timestampProvider: Provider<Long>,
                                                private val filterDao: LinkFilterDao,
                                                private val saveFilterCallbacks: UpdateFilterCallbacks,
                                                @Named("extraFilterId") private val filterId: Long,
                                                @Named("io") private val ioScheduler: Scheduler,
                                                @Named("main") private val mainScheduler: Scheduler) : SaveFilterViewModel {
    override var title: ObservableField<String> = ObservableField("")
    override var filterUrl: ObservableField<String> = ObservableField("")
    override var replaceText: ObservableField<String> = ObservableField("")
    override var replaceSubject: ObservableField<String> = ObservableField("")
    override var encodeUrl: ObservableBoolean = ObservableBoolean(true)

    private var created: Date = Date(0)
    private var loadDisposable: Disposable? = null
    private var saveFilterDisposable: Disposable? = null
    private var deleteFilterDisposable: Disposable? = null

    fun loadFilter() {
        loadDisposable = filterDao.getFilter(filterId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ filter ->
                    created = filter.created
                    title.set(filter.title)
                    filterUrl.set(filter.filterUrl)
                    replaceText.set(filter.replaceText)
                    replaceSubject.set(filter.replaceSubject)
                    encodeUrl.set(!filter.skipEncode)
                })
    }

    fun onDestroy() {
        loadDisposable?.dispose()
        saveFilterDisposable?.dispose()
        deleteFilterDisposable?.dispose()
    }

    fun restoreInstanceState(savedInstanceState: Bundle) {
        created = Date(savedInstanceState.getLong("created"))
        title.set(savedInstanceState.getString("title"))
        filterUrl.set(savedInstanceState.getString("filterUrl"))
        replaceText.set(savedInstanceState.getString("filterUrl"))
        replaceSubject.set(savedInstanceState.getString("replaceSubject"))
        encodeUrl.set(savedInstanceState.getBoolean("encodeUrl"))
    }

    fun saveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putLong("created", created.time)
        savedInstanceState.putString("title", title.get())
        savedInstanceState.putString("filterUrl", filterUrl.get())
        savedInstanceState.putString("replaceText", replaceText.get())
        savedInstanceState.putString("replaceSubject", replaceSubject.get())
        savedInstanceState.putBoolean("encodeUrl", encodeUrl.get())
    }

    private fun deleteFilter() {
        deleteFilterDisposable = filterDao.delete(toLinkFilter())
                .subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
                .subscribe(saveFilterCallbacks::onFilterDeleted)
    }

    private fun updateFilter() {
        saveFilterDisposable = filterDao.update(toLinkFilter())
                .subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
                .subscribe(saveFilterCallbacks::onFilterUpdated)
    }

    private fun toLinkFilter(): LinkFilter {
        return LinkFilter(filterId,
                title.get(),
                filterUrl.get(),
                replaceText.get(),
                replaceSubject.get(),
                created,
                Date(timestampProvider.get()),
                !encodeUrl.get())
    }

    fun cancel() {
        saveFilterCallbacks.onCancelled()
    }

    fun onMenuItemClick(item: MenuItem): Boolean {
        when {
            item.itemId == R.id.menuSave -> {
                updateFilter()
                return true
            }
            item.itemId == R.id.menuDelete -> {
                deleteFilter()
                return true
            }
            else -> return false
        }
    }
}
