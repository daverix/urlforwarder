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

import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
                                                @Named("io") private val ioScheduler: Scheduler,
                                                @Named("main") private val mainScheduler: Scheduler,
                                                @Named("modify") private val idleCounter: IdleCounter) : ViewModel(), SaveFilterViewModel {
    override var title: MutableLiveData<String> = MutableLiveData()
    override var filterUrl: MutableLiveData<String> = MutableLiveData()
    override var replaceText: MutableLiveData<String> = MutableLiveData()
    override var replaceSubject: MutableLiveData<String> = MutableLiveData()
    override var encodeUrl: MutableLiveData<Boolean> = MutableLiveData()
    override var useRegex: MutableLiveData<Boolean> = MutableLiveData()

    val filterId: Long? = null

    private var created: Date = Date(0)
    private var loadDisposable: Disposable? = null
    private var saveFilterDisposable: Disposable? = null
    private var deleteFilterDisposable: Disposable? = null

    fun loadFilter() {
        val filterId = this.filterId ?: return

        loadDisposable = filterDao.getFilter(filterId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { idleCounter.increment() }
                .doAfterTerminate { idleCounter.decrement() }
                .subscribe { filter ->
                    created = filter.created
                    title.value = filter.title
                    filterUrl.value = filter.filterUrl
                    replaceText.value = filter.replaceText
                    replaceSubject.value = filter.replaceSubject
                    encodeUrl.value = !filter.skipEncode
                }
    }

    fun onDestroy() {
        loadDisposable?.dispose()
        saveFilterDisposable?.dispose()
        deleteFilterDisposable?.dispose()
    }

    fun restoreInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.apply {
            created = Date(getLong("created"))
            title.value = getString("title")
            filterUrl.value = getString("filterUrl")
            replaceText.value = getString("filterUrl")
            replaceSubject.value = getString("replaceSubject")
            encodeUrl.value = getBoolean("encodeUrl")
        }
    }

    fun saveInstanceState(outState: Bundle) {
        outState.apply {
            putLong("created", created.time)
            putString("title", title.value ?: "")
            putString("filterUrl", filterUrl.value ?: "")
            putString("replaceText", replaceText.value ?: "")
            putString("replaceSubject", replaceSubject.value ?: "")
            putBoolean("encodeUrl", encodeUrl.value ?: true)
        }
    }

    private fun deleteFilter() {
        deleteFilterDisposable?.dispose()
        deleteFilterDisposable = filterDao.delete(toLinkFilter())
                .doOnSubscribe { idleCounter.increment() }
                .doAfterTerminate { idleCounter.decrement() }
                .subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
                .subscribe {
                    saveFilterCallbacks.onFilterDeleted()
                }
    }

    private fun updateFilter() {
        saveFilterDisposable?.dispose()
        saveFilterDisposable = filterDao.update(toLinkFilter())
                .doOnSubscribe { idleCounter.increment() }
                .doAfterTerminate { idleCounter.decrement() }
                .subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
                .subscribe {
                    saveFilterCallbacks.onFilterUpdated()
                }
    }

    private fun toLinkFilter(): LinkFilter {
        if(filterId == null)
            throw IllegalStateException("filterId not set")

        return LinkFilter(filterId,
                title.value ?: "",
                filterUrl.value ?: "",
                replaceText.value ?: "",
                replaceSubject.value ?: "",
                created,
                Date(timestampProvider.get()),
                !(encodeUrl.value ?: true))
    }

    fun cancel() {
        saveFilterCallbacks.onCancelled()
    }

    fun onMenuItemClick(item: MenuItem): Boolean {
        return when {
            item.itemId == R.id.menuSave -> {
                updateFilter()
                true
            }
            item.itemId == R.id.menuDelete -> {
                deleteFilter()
                true
            }
            else -> false
        }
    }
}
