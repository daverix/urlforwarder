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

import android.databinding.DataBindingUtil
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.DaggerFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import net.daverix.urlforward.dao.LinkFilter
import net.daverix.urlforward.dao.LinkFilterDao
import net.daverix.urlforward.dao.queryAll
import net.daverix.urlforward.databinding.FilterRowBinding
import net.daverix.urlforward.databinding.FiltersFragmentBinding
import javax.inject.Inject
import javax.inject.Named

class FiltersFragment : DaggerFragment() {
    val TAG = "FiltersFragment"

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

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FiltersFragmentBinding>(inflater!!,
                R.layout.filters_fragment,
                container, false)
        binding.filters.layoutManager = LinearLayoutManager(activity)
        binding.filters.adapter = adapter
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        adapter.attachObserver()

        Log.d("FiltersFragment", "resumed")

        filtersDisposable?.dispose()
        filtersDisposable = dao.queryAll()
                .doOnSubscribe({ idleCounter.increment()})
                .doAfterTerminate { idleCounter.decrement() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    viewModel.filtersVisible.set(true)

                    filters.updateList(it,
                            { (id), viewModel -> id == viewModel.id },
                            { it.toViewModel() })
                }, { e ->
                    Log.e(TAG, "Could not retrieve filters", e)
                })
    }

    override fun onPause() {
        super.onPause()

        filtersDisposable?.dispose()
        adapter.detachObserver()
    }

    fun LinkFilter.toViewModel(): FilterRowViewModel {
        return FilterRowViewModel(listener, title, filterUrl, id)
    }
}
