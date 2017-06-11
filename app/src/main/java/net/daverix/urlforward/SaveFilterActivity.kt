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

import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.Completable
import io.reactivex.CompletableSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import net.daverix.urlforward.databinding.SaveFilterActivityBinding
import net.daverix.urlforward.db.LinkFilterStorage
import javax.inject.Inject

class SaveFilterActivity : DaggerAppCompatActivity() {
    private var fragment: SaveFilterFragment? = null
    private var compositeDisposable: CompositeDisposable? = null

    @Inject @JvmField
    var storage: LinkFilterStorage? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        compositeDisposable = CompositeDisposable()

        val binding = DataBindingUtil.setContentView<SaveFilterActivityBinding>(this, R.layout.save_filter_activity)
        binding.toolbar.setNavigationOnClickListener { _ ->
            val intent = Intent(this@SaveFilterActivity, FiltersActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
        fragment = supportFragmentManager.findFragmentById(R.id.saveFilterFragment) as SaveFilterFragment

        val intent = intent
        if (intent != null && Intent.ACTION_INSERT == intent.action) {
            binding.toolbar.setTitle(R.string.create_filter)
            binding.toolbar.inflateMenu(R.menu.fragment_save_filter)

            if (fragment == null) {
                fragment = SaveFilterFragment.newCreateInstance()
                supportFragmentManager.beginTransaction().add(R.id.saveFilterFragment, fragment).commit()
            }
        } else if (intent != null && Intent.ACTION_EDIT == intent.action) {
            binding.toolbar.setTitle(R.string.edit_filter)
            binding.toolbar.inflateMenu(R.menu.fragment_edit_filter)

            if (fragment == null) {
                fragment = SaveFilterFragment.newUpdateInstance(intent.data)
                supportFragmentManager.beginTransaction().add(R.id.saveFilterFragment, fragment).commit()
            }
        }

        binding.toolbar.setOnMenuItemClickListener { item ->
            val theIntent = getIntent()

            when (item.itemId) {
                R.id.menuSave -> {
                    if (theIntent?.action == Intent.ACTION_INSERT) {
                        createFilter(fragment!!.filter!!)
                    } else if (theIntent?.action == Intent.ACTION_EDIT) {
                        updateFilter(fragment!!.filter!!)
                    }
                    true
                }
                R.id.menuDelete -> {
                    deleteFilter(theIntent!!.data)
                    true
                }
                else -> false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        if (compositeDisposable != null) {
            compositeDisposable!!.dispose()
        }
    }

    private fun createFilter(linkFilter: LinkFilter) {
        compositeDisposable!!.add(storage!!.insert(linkFilter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this::idlingResource)
                .subscribe(this::finish))
    }

    private fun updateFilter(linkFilter: LinkFilter) {
        compositeDisposable!!.add(storage!!.update(linkFilter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this::idlingResource)
                .subscribe(this::finish))
    }

    private fun deleteFilter(uri: Uri) {
        compositeDisposable!!.add(storage!!.delete(java.lang.Long.parseLong(uri.lastPathSegment))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this::idlingResource)
                .subscribe(this::finish))
    }

    private fun idlingResource(completable: Completable): CompletableSource {
        val idlingResource = (application as UrlForwarderApplication)
                .getModifyFilterIdlingResource() ?: return completable

        return completable
                .doOnSubscribe { _ -> idlingResource.setIdle(false) }
                .doFinally { idlingResource.setIdle(true) }
    }
}
