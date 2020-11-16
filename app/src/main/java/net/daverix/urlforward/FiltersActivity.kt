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
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import net.daverix.urlforward.FiltersFragment.FilterSelectedListener
import net.daverix.urlforward.databinding.FiltersActivityBinding
import net.daverix.urlforward.db.UrlForwarderContract.UrlFilters

class FiltersActivity : AppCompatActivity(), FilterSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView<FiltersActivityBinding>(this, R.layout.filters_activity).run {
            btnAddFilter.setOnClickListener {
                startActivity(Intent(Intent.ACTION_INSERT, UrlFilters.CONTENT_URI))
            }
        }
    }

    override fun onFilterSelected(id: Long) {
        startActivity(Intent(Intent.ACTION_EDIT, Uri.withAppendedPath(UrlFilters.CONTENT_URI, id.toString())))
    }
}
