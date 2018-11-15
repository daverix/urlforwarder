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

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import dagger.android.support.DaggerAppCompatActivity
import net.daverix.urlforward.databinding.UpdateFilterActivityBinding

class InsertFilterActivity : DaggerAppCompatActivity(), InsertFilterCallbacks {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DataBindingUtil.setContentView<UpdateFilterActivityBinding>(this,
                R.layout.insert_filter_activity)

        if (supportFragmentManager.findFragmentById(R.id.saveFilterFragment) == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.saveFilterFragment, InsertFilterFragment.newInstance())
                    .commit()
        }
    }

    override fun onFilterInserted() {
        finish()
    }

    override fun onCancelled() {
        finish()
    }
}
