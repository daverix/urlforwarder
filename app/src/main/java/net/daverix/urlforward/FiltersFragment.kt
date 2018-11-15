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
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import dagger.android.support.DaggerFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import net.daverix.urlforward.dao.LinkFilterDao
import net.daverix.urlforward.dao.queryAll
import net.daverix.urlforward.databinding.FilterRowBinding
import net.daverix.urlforward.databinding.FiltersFragmentBinding
import javax.inject.Inject
import javax.inject.Named

class FiltersFragment : DaggerFragment() {
    private lateinit var listener: OnFilterClickedListener
    private lateinit var adapter: SimpleBindingAdapter<FilterRowBinding, FilterRowViewModel>

    private var filtersDisposable: Disposable? = null
    private var filters: ObservableList<FilterRowViewModel> = ObservableArrayList()

    @set:Inject lateinit var viewModel: FiltersViewModel
    @set:Inject lateinit var dao: LinkFilterDao
    @set:Inject @setparam:Named("load") lateinit var idleCounter: IdleCounter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        listener = activity as OnFilterClickedListener
        adapter = SimpleBindingAdapter(filters, FilterRowBinder(LayoutInflater.from(activity)))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FiltersFragmentBinding>(inflater,
                R.layout.filters_fragment,
                container, false)?.apply {
            //filters.adapter = adapter
            this.viewModel = viewModel
        }
        return binding!!.root
    }

    override fun onResume() {
        super.onResume()

        adapter.attachObserver()

        Log.d("FiltersFragment", "resumed")

        filtersDisposable?.dispose()
        filtersDisposable = dao.queryAll()
                .doOnSubscribe { idleCounter.increment() }
                .doAfterTerminate { idleCounter.decrement() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    viewModel.filtersVisible.set(true)

                    filters.updateList(it,
                            { (id), viewModel -> id == viewModel.id },
                            { FilterRowViewModel(listener, it.title, it.filterUrl, it.id) })
                }, { e ->
                    Log.e(TAG, "Could not retrieve filters", e)
                })
    }

    override fun onPause() {
        super.onPause()

        filtersDisposable?.dispose()
        adapter.detachObserver()
    }

    companion object {
        const val TAG = "FiltersFragment"
    }
}
