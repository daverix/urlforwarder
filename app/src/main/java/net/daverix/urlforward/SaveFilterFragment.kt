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

import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.view.*
import dagger.android.support.DaggerFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import net.daverix.urlforward.databinding.SaveFilterFragmentBinding
import net.daverix.urlforward.db.LinkFilterStorage
import java.lang.Long.parseLong
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Provider

class SaveFilterFragment : DaggerFragment() {
    var filter: LinkFilter? = null
    private var uri: Uri? = null
    private var state: Int = 0
    private var loadFilterDisposable: Disposable? = null

    @JvmField @field:Inject
    var storage: LinkFilterStorage? = null

    @JvmField @field:[Inject Named("timestamp")]
    var timestampProvider: Provider<Long>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

        val args = arguments
        if (args != null) {
            uri = args.getParcelable<Uri>(ARG_URI)
            state = args.getInt(ARG_STATE)
        }

        if (savedInstanceState != null) {
            filter = savedInstanceState.getParcelable<LinkFilter>("filter")
        } else {
            filter = LinkFilter()

            if (state == STATE_CREATE) {
                filter!!.created = timestampProvider!!.get()
                filter!!.filterUrl = "http://example.com/?url=@url&subject=@subject"
                filter!!.replaceText = "@url"
                filter!!.replaceSubject = "@subject"
                filter!!.encoded = true
            }
        }
    }

    override fun onDestroy() {
        if (loadFilterDisposable != null) {
            loadFilterDisposable!!.dispose()
        }

        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState!!.putParcelable("filter", filter)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<SaveFilterFragmentBinding>(inflater!!, R.layout.save_filter_fragment, container, false)
        binding.filter = filter
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (state != STATE_CREATE) {
            filter!!.id = parseLong(uri!!.lastPathSegment)

            loadFilterDisposable = storage!!.getFilter(filter!!.id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ linkFilter -> filter!!.update(linkFilter) })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater!!.inflate(R.menu.fragment_save_filter, menu)
    }

    companion object {
        const val ARG_STATE = "state"
        const val ARG_URI = "uri"
        const val STATE_CREATE = 0
        const val STATE_UPDATE = 1

        fun newCreateInstance(): SaveFilterFragment {
            val saveFilterFragment = SaveFilterFragment()
            val args = Bundle()
            args.putInt(ARG_STATE, STATE_CREATE)
            saveFilterFragment.arguments = args
            return saveFilterFragment
        }

        fun newUpdateInstance(uri: Uri): SaveFilterFragment {
            val saveFilterFragment = SaveFilterFragment()
            val args = Bundle()
            args.putInt(ARG_STATE, STATE_UPDATE)
            args.putParcelable(ARG_URI, uri)
            saveFilterFragment.arguments = args
            return saveFilterFragment
        }
    }
}
