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

import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import net.daverix.urlforward.databinding.SaveFilterFragmentBinding
import net.daverix.urlforward.db.UrlForwarderContract.UrlFilterColumns

class SaveFilterFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor> {
    var filter: LinkFilter? = null
        private set

    private var uri: Uri? = null
    private var state = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        val args = arguments
        if (args != null) {
            uri = args.getParcelable(ARG_URI)
            state = args.getInt(ARG_STATE)
        }
        if (savedInstanceState != null) {
            filter = savedInstanceState.getParcelable("filter")
        } else {
            filter = LinkFilter()
            filter!!.created = System.currentTimeMillis()
            if (state == STATE_CREATE) {
                filter!!.filterUrl = "https://example.com/?url=@url&subject=@subject"
                filter!!.replaceText = "@url"
                filter!!.replaceSubject = "@subject"
                filter!!.encoded = true
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("filter", filter)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: SaveFilterFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.save_filter_fragment, container, false)
        binding.filter = filter
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (state == STATE_UPDATE && savedInstanceState == null) {
            loaderManager.restartLoader(LOADER_LOAD_FILTER, null, this)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_save_filter, menu)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return when (id) {
            LOADER_LOAD_FILTER -> CursorLoader(activity!!, uri!!, arrayOf(
                    UrlFilterColumns.TITLE,
                    UrlFilterColumns.FILTER,
                    UrlFilterColumns.REPLACE_TEXT,
                    UrlFilterColumns.CREATED,
                    UrlFilterColumns.SKIP_ENCODE,
                    UrlFilterColumns.REPLACE_SUBJECT
            ), null, null, null)
            else -> error("no loader for id $id")
        }
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor) {
        when (loader.id) {
            LOADER_LOAD_FILTER -> if (data.moveToFirst()) {
                filter?.title = data.getString(0)
                filter?.filterUrl = data.getString(1)
                filter?.replaceText = data.getString(2)
                filter?.created = data.getLong(3)
                filter?.encoded = data.getShort(4).toInt() != 1
                filter?.replaceSubject = data.getString(5)
            }
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor?>) {}

    companion object {
        private const val LOADER_LOAD_FILTER = 0
        private const val ARG_STATE = "state"
        private const val ARG_URI = "uri"
        private const val STATE_CREATE = 0
        private const val STATE_UPDATE = 1

        @JvmStatic
        fun newCreateInstance(): SaveFilterFragment {
            val saveFilterFragment = SaveFilterFragment()
            val args = Bundle()
            args.putInt(ARG_STATE, STATE_CREATE)
            saveFilterFragment.arguments = args
            return saveFilterFragment
        }

        @JvmStatic
        fun newUpdateInstance(uri: Uri?): SaveFilterFragment {
            val saveFilterFragment = SaveFilterFragment()
            val args = Bundle()
            args.putInt(ARG_STATE, STATE_UPDATE)
            args.putParcelable(ARG_URI, uri)
            saveFilterFragment.arguments = args
            return saveFilterFragment
        }
    }
}