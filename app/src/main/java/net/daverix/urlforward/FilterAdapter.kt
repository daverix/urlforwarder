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

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import net.daverix.urlforward.databinding.FilterRowBinding

class FilterAdapter internal constructor(private val inflater: LayoutInflater,
                                         private val filters: List<FilterRowViewModel>) : RecyclerView.Adapter<BindingHolder<FilterRowBinding>>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): BindingHolder<FilterRowBinding> {
        val binding = DataBindingUtil.inflate<FilterRowBinding>(inflater,
                R.layout.filter_row, viewGroup, false)
        return BindingHolder(binding)
    }

    override fun onBindViewHolder(holder: BindingHolder<FilterRowBinding>, position: Int) {
        val filter = filters[position]
        holder.binding.filter = filter
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return filters.size
    }
}