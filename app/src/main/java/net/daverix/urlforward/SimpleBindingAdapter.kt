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

import android.view.ViewGroup
import androidx.databinding.ObservableList
import androidx.databinding.ObservableList.OnListChangedCallback
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class SimpleBindingAdapter<TBinding : ViewDataBinding, TItem>(private val items: ObservableList<TItem>,
                                                              private val binder: Binder<TBinding, TItem>)
    : RecyclerView.Adapter<SimpleBindingAdapter.BindingHolder<TBinding>>() {

    private val listCallback: OnListChangedCallback<ObservableList<TItem>> = object : OnListChangedCallback<ObservableList<TItem>>() {
        override fun onItemRangeRemoved(sender: ObservableList<TItem>?, positionStart: Int, itemCount: Int) {
            notifyItemRangeRemoved(positionStart, itemCount)
        }

        override fun onItemRangeMoved(sender: ObservableList<TItem>?, fromPosition: Int, toPosition: Int, itemCount: Int) {
            for (i in itemCount downTo 0) {
                notifyItemMoved(fromPosition + i, toPosition + i)
            }
        }

        override fun onChanged(sender: ObservableList<TItem>?) {
            notifyDataSetChanged()
        }

        override fun onItemRangeChanged(sender: ObservableList<TItem>?, positionStart: Int, itemCount: Int) {
            notifyItemRangeChanged(positionStart, itemCount)
        }

        override fun onItemRangeInserted(sender: ObservableList<TItem>?, positionStart: Int, itemCount: Int) {
            notifyItemRangeInserted(positionStart, itemCount)
        }
    }

    fun attachObserver() {
        items.addOnListChangedCallback(listCallback)
    }

    fun detachObserver() {
        items.removeOnListChangedCallback(listCallback)
    }

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