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

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

class SimpleBindingAdapter<TBinding : ViewDataBinding, in TItem>(private val items: List<TItem>,
                                                                 private val binder: Binder<TBinding, TItem>)
    : RecyclerView.Adapter<SimpleBindingAdapter.BindingHolder<TBinding>>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): BindingHolder<TBinding> {
        return BindingHolder(binder.createBinding(viewGroup, viewType))
    }

    override fun onBindViewHolder(holder: BindingHolder<TBinding>, position: Int) {
        binder.bind(holder.binding, items[position])
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class BindingHolder<out T : ViewDataBinding>(val binding: T) : RecyclerView.ViewHolder(binding.root)
}

interface Binder<TBinding, in TItem> {
    fun bind(binding: TBinding, item: TItem)

    fun createBinding(viewGroup: ViewGroup, viewType: Int): TBinding
}