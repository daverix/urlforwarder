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

import android.R
import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.ListFragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import net.daverix.urlforward.db.UrlForwarderContract.UrlFilterColumns
import net.daverix.urlforward.db.UrlForwarderContract.UrlFilters
import java.util.*

class LinksFragment : ListFragment(), LoaderManager.LoaderCallbacks<Cursor> {
    private var listener: LinksFragmentListener? = null
    private var mapper: LinkFilterMapper? = null
    override fun onAttach(activity: Context) {
        super.onAttach(activity)
        listener = activity as LinksFragmentListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mapper = LinkFilterMapperImpl()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loaderManager.initLoader(LOADER_LOAD_FILTERS, null, this)
    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)
        val filter = l.getItemAtPosition(position) as LinkFilter
        if (listener != null) {
            listener!!.onLinkClick(filter)
        }
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return when (id) {
            LOADER_LOAD_FILTERS -> CursorLoader(activity!!, UrlFilters.CONTENT_URI,
                    mapper!!.columns, null, null, UrlFilterColumns.TITLE)
            else -> error("loader not for id $id")
        }
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor) {
        when (loader.id) {
            LOADER_LOAD_FILTERS -> listAdapter = ArrayAdapter(activity!!,
                    R.layout.simple_list_item_1,
                    mapFilters(data))
        }
    }

    private fun mapFilters(cursor: Cursor?): List<LinkFilter> {
        val filters: MutableList<LinkFilter> = ArrayList()
        var i = 0
        while (cursor != null && cursor.moveToPosition(i)) {
            filters.add(mapper!!.mapFilter(cursor))
            i++
        }
        return filters
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {}

    interface LinksFragmentListener {
        fun onLinkClick(filter: LinkFilter)
    }

    companion object {
        private const val LOADER_LOAD_FILTERS = 0
    }
}