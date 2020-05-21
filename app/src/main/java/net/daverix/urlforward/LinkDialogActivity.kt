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
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import dagger.android.support.DaggerAppCompatActivity
import net.daverix.urlforward.databinding.LinkDialogActivityBinding
import javax.inject.Inject

class LinkDialogActivity : DaggerAppCompatActivity() {
    @Inject
    lateinit var viewModelProvider: LinkDialogViewModel.Factory

    private val viewModel: LinkDialogViewModel by viewModels {
        factory {
            val url = intent.getStringExtra(Intent.EXTRA_TEXT) ?: ""
            val subject = intent.getStringExtra(Intent.EXTRA_SUBJECT) ?: ""

            viewModelProvider.create(url, subject)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val url = intent.getStringExtra(Intent.EXTRA_TEXT) ?: ""
        if (url.isEmpty()) {
            Toast.makeText(this, "No url found in shared data!", Toast.LENGTH_SHORT).show()
            Log.e("LinkDialogActivity", "No StringExtra with url in intent")
            finish()
            return
        }

        val binding = DataBindingUtil.setContentView<LinkDialogActivityBinding>(this, R.layout.link_dialog_activity)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        lifecycleScope.launchWhenCreated {
            for(event in viewModel.events) {
                if(event is OpenLink) {
                    try {
                        startActivity(Intent(Intent.ACTION_VIEW, event.uri))
                        finish()
                    } catch (ex: Exception) {
                        Log.e("LinkDialogActivity", "error launching intent with url ${event.uri}", ex)
                    }
                }
            }
        }
    }
}
