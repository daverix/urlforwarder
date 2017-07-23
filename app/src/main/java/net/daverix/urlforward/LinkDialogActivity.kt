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

import android.content.Intent
import android.databinding.DataBindingUtil
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import net.daverix.urlforward.dao.LinkFilterDao
import net.daverix.urlforward.dao.queryAll
import net.daverix.urlforward.databinding.LinkDialogActivityBinding
import net.daverix.urlforward.databinding.LinkRowBinding
import net.daverix.urlforward.filter.UriFilterCombiner
import javax.inject.Inject
import javax.inject.Named

class LinkDialogActivity : DaggerAppCompatActivity(), OnFilterClickedListener {
    private var url: String? = null
    private var subject: String? = null
    private var combinerDisposable: Disposable? = null

    private val filters: ObservableList<LinkRowViewModel> = ObservableArrayList()
    private lateinit var adapter: SimpleBindingAdapter<LinkRowBinding, LinkRowViewModel>
    private var filtersDisposable: Disposable? = null

    @set:Inject
    lateinit var dao: LinkFilterDao

    @set:Inject @setparam:Named("load")
    lateinit var idleCounter: IdleCounter

    @set:Inject
    lateinit var mUriFilterCombiner: UriFilterCombiner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = intent
        if (intent == null) {
            Toast.makeText(this, "Invalid intent!", Toast.LENGTH_SHORT).show()
            Log.e("LinkDialogActivity", "Intent empty")
            finish()
            return
        }

        url = intent.getStringExtra(Intent.EXTRA_TEXT)
        subject = intent.getStringExtra(Intent.EXTRA_SUBJECT)
        if (url == null || url!!.isEmpty()) {
            Toast.makeText(this, "No url found in shared data!", Toast.LENGTH_SHORT).show()
            Log.e("LinkDialogActivity", "No StringExtra with url in intent")
            finish()
            return
        }

        val binding = DataBindingUtil.setContentView<LinkDialogActivityBinding>(this, R.layout.link_dialog_activity)
        adapter = SimpleBindingAdapter(filters, LinkRowBinder(layoutInflater))
        binding.links.adapter = adapter
    }

    override fun onResume() {
        super.onResume()

        adapter.attachObserver()

        filtersDisposable?.dispose()
        filtersDisposable = dao.queryAll()
                .doOnSubscribe({ idleCounter.increment() })
                .doAfterTerminate { idleCounter.decrement() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { items ->
                    filters.updateList(items,
                            { (id), viewModel -> id == viewModel.id },
                            { (id, title) ->
                                LinkRowViewModel(this@LinkDialogActivity, title, id)
                            })
                }
    }

    override fun onPause() {
        super.onPause()

        combinerDisposable?.dispose()
        filtersDisposable?.dispose()
        adapter.detachObserver()
    }

    override fun onFilterClicked(filterId: Long) {
        combinerDisposable?.dispose()
        combinerDisposable = mUriFilterCombiner.create(filterId, url!!, subject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ uri ->
                    startActivity(Intent(Intent.ACTION_VIEW, uri))
                    finish()
                }, {
                    Log.e("LinkDialogActivity", "error launching intent with url " + url, it)
                })
    }
}
