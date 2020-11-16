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
import android.database.Cursor
import android.os.Bundle
import android.provider.BaseColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.daverix.urlforward.databinding.FilterRowBinding
import net.daverix.urlforward.databinding.FiltersFragmentBinding
import net.daverix.urlforward.db.UrlForwarderContract.UrlFilterColumns
import net.daverix.urlforward.db.UrlForwarderContract.UrlFilters
import java.util.*

class FiltersFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor> {
    private lateinit var listener: FilterSelectedListener

    private lateinit var viewModel: FiltersViewModel
    private lateinit var adapter: FilterAdapter

    override fun onAttach(activity: Context) {
        super.onAttach(activity)
        listener = activity as FilterSelectedListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = FiltersViewModel()
        adapter = FilterAdapter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            DataBindingUtil.inflate<FiltersFragmentBinding>(inflater, R.layout.filters_fragment, container, false).apply {
                list.layoutManager = LinearLayoutManager(activity)
                list.adapter = adapter
                filters = viewModel
            }?.root

    override fun onResume() {
        super.onResume()
        Log.d("FiltersFragment", "resumed")
        loaderManager.initLoader(LOADER_LOAD_FILTERS, null, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        Log.d("FiltersFragment", "create loader $id")
        return when (id) {
            LOADER_LOAD_FILTERS -> CursorLoader(activity!!, UrlFilters.CONTENT_URI, arrayOf(
                    BaseColumns._ID,
                    UrlFilterColumns.TITLE,
                    UrlFilterColumns.FILTER
            ), null, null, UrlFilterColumns.TITLE)
            else -> error("no loader for id $id")
        }
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor) {
        Log.d("FiltersFragment", "loader " + loader.id + " finished, items: " + data.count)
        when (loader.id) {
            LOADER_LOAD_FILTERS -> {
                val items: List<FilterRowViewModel>
                if (data.count > 0) {
                    items = mapListFilters(data)
                    viewModel.filtersVisible.set(true)
                } else {
                    items = ArrayList()
                    Log.d("FiltersFragment", "list is empty")
                    viewModel.filtersVisible.set(false)
                }
                Log.d("FiltersFragment", "updating adapter")
                adapter.setFilters(items)
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        Log.d("FiltersFragment", "loader " + loader.id + " reset")
    }

    interface FilterSelectedListener {
        fun onFilterSelected(id: Long)
    }

    private fun mapListFilters(cursor: Cursor): List<FilterRowViewModel> {
        val items: MutableList<FilterRowViewModel> = ArrayList(cursor.count)

        while (cursor.moveToNext()) {
            items.add(FilterRowViewModel(listener,
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getLong(0)))
        }
        return items
    }

    private inner class FilterAdapter : RecyclerView.Adapter<BindingHolder<FilterRowBinding>>() {
        private var filters: List<FilterRowViewModel>

        fun setFilters(filters: List<FilterRowViewModel>) {
            this.filters = filters
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): BindingHolder<FilterRowBinding> {
            val binding: FilterRowBinding = DataBindingUtil.inflate(LayoutInflater.from(activity),
                    R.layout.filter_row, viewGroup, false)
            return BindingHolder(binding)
        }

        override fun onBindViewHolder(holder: BindingHolder<FilterRowBinding>, position: Int) {
            val filter = filters[position]
            holder.binding.filter = filter
            holder.binding.executePendingBindings()
        }

        override fun getItemCount(): Int {
            return filters.size
        }

        init {
            filters = ArrayList()
        }
    }

    companion object {
        private const val LOADER_LOAD_FILTERS = 1
    }
}
