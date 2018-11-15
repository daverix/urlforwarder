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

import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.MutableLiveData
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
                                                @Named("modify") private val idleCounter: IdleCounter) : SaveFilterViewModel {
    override val title: MutableLiveData<String> = MutableLiveData()
    override val filterUrl: MutableLiveData<String> = MutableLiveData()
    override val replaceText: MutableLiveData<String> = MutableLiveData()
    override val replaceSubject: MutableLiveData<String> = MutableLiveData()
    override val encodeUrl: MutableLiveData<Boolean> = MutableLiveData()
    override val useRegex: MutableLiveData<Boolean> = MutableLiveData()

    private var saveFilterDisposable: Disposable? = null

    fun loadFilter() {
        filterUrl.value = "http://example.com/?url=@url&subject=@subject"
        replaceText.value = "@url"
        replaceSubject.value = "@subject"
        encodeUrl.value = true
    }

    fun onDestroy() {
        saveFilterDisposable?.dispose()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    fun restoreInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.apply {
            title.value = getString("title")
            filterUrl.value = getString("filterUrl")
            replaceText.value = getString("filterUrl")
            replaceSubject.value = getString("replaceSubject")
            encodeUrl.value = getBoolean("encodeUrl")
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    fun saveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.apply {
            putString("title", title.value)
            putString("filterUrl", filterUrl.value)
            putString("replaceText", replaceText.value)
            putString("replaceSubject", replaceSubject.value)
            putBoolean("encodeUrl", encodeUrl.value ?: true)
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
                title.value ?: "",
                filterUrl.value ?: "",
                replaceText.value ?: "",
                replaceSubject.value ?: "",
                now,
                now,
                encodeUrl.value ?: true)
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
