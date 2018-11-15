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
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import net.daverix.urlforward.databinding.FiltersActivityBinding

class FiltersActivity : AppCompatActivity(), OnFilterClickedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView<FiltersActivityBinding>(this, R.layout.filters_activity)?.apply {
            btnAddFilter.setOnClickListener {
                startActivity(Intent(this@FiltersActivity, InsertFilterActivity::class.java))
            }
        }
    }

    override fun onFilterClicked(filterId: Long) {
        startActivity(Intent(this, UpdateFilterActivity::class.java).apply {
            putExtra(UpdateFilterActivity.FILTER_ID, filterId)
        })
    }
}
