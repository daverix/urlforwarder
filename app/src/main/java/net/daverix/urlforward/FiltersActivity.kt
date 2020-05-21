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

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import dagger.android.support.DaggerAppCompatActivity
import net.daverix.urlforward.SaveFilterActivity.Companion.EXTRA_FILTER_ID
import net.daverix.urlforward.databinding.FiltersActivityBinding
import javax.inject.Inject
import javax.inject.Provider

class FiltersActivity : DaggerAppCompatActivity() {
    @set:Inject
    lateinit var viewModelProvider: Provider<FiltersViewModel>

    private val viewModel by viewModels<FiltersViewModel> {
        factory {
            viewModelProvider.get()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<FiltersActivityBinding>(this, R.layout.filters_activity)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        lifecycleScope.launchWhenStarted {
            for (event in viewModel.events) {
                when (event) {
                    is EditFilter -> {
                        val intent = Intent(this@FiltersActivity, SaveFilterActivity::class.java).apply {
                            action = Intent.ACTION_EDIT
                            putExtra(EXTRA_FILTER_ID, event.filterId)
                        }
                        startActivity(intent)
                    }
                    is CreateFilter -> {
                        val intent = Intent(this@FiltersActivity, SaveFilterActivity::class.java).apply {
                            action = Intent.ACTION_INSERT
                        }
                        startActivity(intent)
                    }
                }
            }
        }
    }
}
