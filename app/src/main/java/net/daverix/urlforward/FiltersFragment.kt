/*
    UrlForwarder makes it possible to use bookmarklets on Android
    Copyright (C) 2016 David Laurell

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

import android.content.Context
import android.databinding.DataBindingUtil
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
import net.daverix.urlforward.databinding.FiltersFragmentBinding
import net.daverix.urlforward.db.LinkFilterStorage
import javax.inject.Inject

class FiltersFragment : DaggerFragment() {
    val TAG = "FiltersFragment"

    private var listener: FilterSelectedListener? = null
    private var adapter: FilterAdapter? = null
    private var filtersDisposable: Disposable? = null

    @Inject @JvmField internal var viewModel: FiltersViewModel? = null
    @Inject @JvmField internal var storage: LinkFilterStorage? = null

    override fun onAttach(activity: Context?) {
        super.onAttach(activity)

        listener = activity as FilterSelectedListener?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = FilterAdapter(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FiltersFragmentBinding>(inflater!!, R.layout.filters_fragment, container, false)
        binding.list.layoutManager = LinearLayoutManager(activity)
        binding.list.adapter = adapter
        binding.filters = viewModel
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        Log.d("FiltersFragment", "resumed")

        if (listener != null) {
            filtersDisposable = storage!!.queryAll()
                    .map { filter -> FilterRowViewModel(listener!!,
                            filter.title,
                            filter.filterUrl,
                            filter.id) }
                    .toList()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::updateItems, { e ->
                        Log.e(TAG, "Could not retrieve filters", e)
                    })
        }
    }

    override fun onPause() {
        super.onPause()

        if (filtersDisposable != null) {
            filtersDisposable!!.dispose()
            filtersDisposable = null
        }
    }

    fun updateItems(items: List<FilterRowViewModel>) {
        viewModel!!.filtersVisible.set(items.isNotEmpty())

        adapter!!.filters = items
        adapter!!.notifyDataSetChanged()
    }

    interface FilterSelectedListener {
        fun onFilterSelected(id: Long)
    }

}
