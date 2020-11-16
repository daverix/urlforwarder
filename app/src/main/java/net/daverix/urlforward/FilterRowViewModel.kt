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

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import net.daverix.urlforward.FiltersFragment.FilterSelectedListener

class FilterRowViewModel(
        private val listener: FilterSelectedListener,
        @get:Bindable val title: String,
        @get:Bindable val filterUrl: String,
        private val id: Long
) : BaseObservable() {
    fun onClick() {
        listener.onFilterSelected(id)
    }
}
