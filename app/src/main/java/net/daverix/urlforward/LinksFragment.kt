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

import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.DaggerFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import net.daverix.urlforward.dao.LinkFilterDao
import net.daverix.urlforward.databinding.FilterRowBinding
import net.daverix.urlforward.databinding.LinksFragmentBinding
import javax.inject.Inject

class LinksFragment : DaggerFragment() {
    private val filters: ObservableList<FilterRowViewModel> = ObservableArrayList()
    private lateinit var listener: OnFilterClickedListener
    private lateinit var adapter: SimpleBindingAdapter<FilterRowBinding, FilterRowViewModel>
    private var disposable: Disposable? = null

    @set:Inject
    lateinit var dao: LinkFilterDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        listener = activity as OnFilterClickedListener
        adapter = SimpleBindingAdapter(filters, FilterRowBinder(LayoutInflater.from(activity)))
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = LinksFragmentBinding.inflate(inflater!!, container, false)
        binding.list.layoutManager = LinearLayoutManager(activity)
        binding.list.adapter = adapter
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        adapter.attachObserver()

        disposable?.dispose()
        disposable = dao.queryAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { items ->
                    filters.updateList(items,
                            { (id), viewModel -> id == viewModel.id },
                            { (id, title, filterUrl) ->
                                FilterRowViewModel(listener,
                                        title,
                                        filterUrl,
                                        id)
                            })
                }
    }

    override fun onPause() {
        super.onPause()

        if (disposable != null && !disposable!!.isDisposed) {
            disposable!!.dispose()
            disposable = null
        }

        adapter.detachObserver()
    }
}
