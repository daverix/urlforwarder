/*
    UrlForwarder makes it possible to use bookmarklets on Android
    Copyright (C) 2018 David Laurell

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

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import dagger.android.support.DaggerAppCompatActivity
import net.daverix.urlforward.databinding.SaveFilterActivityBinding
import javax.inject.Inject
import javax.inject.Provider

class SaveFilterActivity : DaggerAppCompatActivity() {
    @Inject
    lateinit var insertViewModelProvider: Provider<InsertFilterViewModel>

    @Inject
    lateinit var updateViewModelProvider: UpdateFilterViewModel.Factory

    private val viewModel by viewModels<SaveFilterViewModel> {
        factory {
            when(intent.action) {
                Intent.ACTION_EDIT -> updateViewModelProvider.create(intent.getLongExtra(EXTRA_FILTER_ID, -1))
                Intent.ACTION_INSERT -> insertViewModelProvider.get()
                else -> throw IllegalStateException("unknown action ${intent.action}")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<SaveFilterActivityBinding>(this,
                R.layout.save_filter_activity)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val menuRes = when(intent.action) {
            Intent.ACTION_EDIT -> R.menu.fragment_update_filter
            Intent.ACTION_INSERT -> R.menu.fragment_insert_filter
            else -> throw IllegalStateException("unknown action ${intent.action}")
        }
        binding.toolbar.inflateMenu(menuRes)
        binding.toolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.menuSave -> {
                    viewModel.saveFilter()
                    true
                }
                R.id.menuDelete -> {
                    viewModel.deleteFilter()
                    true
                }
                else -> false
            }
        }

        lifecycleScope.launchWhenCreated {
            for(event in viewModel.events) {
                when(event) {
                    is Saved -> {
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                    is Deleted -> {
                        setResult(Activity.RESULT_CANCELED)
                        finish()
                    }
                    is Cancel -> {
                        setResult(Activity.RESULT_CANCELED)
                        finish()
                    }
                }
            }
        }
    }

    companion object {
        const val EXTRA_FILTER_ID = "filterId"
    }
}
