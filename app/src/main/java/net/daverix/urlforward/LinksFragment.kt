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
import android.view.View
import android.widget.ListView
import dagger.android.support.DaggerFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import net.daverix.urlforward.db.LinkFilterStorage
import javax.inject.Inject

class LinksFragment : DaggerFragment() {
    private var listener: LinksFragmentListener? = null
    private var disposable: Disposable? = null

    @Inject @JvmField
    var storage: LinkFilterStorage? = null

    override fun onAttach(activity: Context?) {
        super.onAttach(activity)

        listener = activity as LinksFragmentListener?
    }

    fun onListItemClick(l: ListView?, v: View?, position: Int, id: Long) {
        //super.onListItemClick(l, v, position, id)

        val filter = l!!.getItemAtPosition(position) as LinkFilter
        if (listener != null) {
            listener!!.onLinkClick(filter)
        }
    }

    override fun onResume() {
        super.onResume()

        disposable = storage!!.queryAll()
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { items ->
                    /*listAdapter = ArrayAdapter(activity,
                            android.R.layout.simple_list_item_1,
                            items)*/
                }
    }

    override fun onPause() {
        super.onPause()

        if (disposable != null && !disposable!!.isDisposed) {
            disposable!!.dispose()
            disposable = null
        }
    }

    interface LinksFragmentListener {
        fun onLinkClick(filter: LinkFilter)
    }
}
