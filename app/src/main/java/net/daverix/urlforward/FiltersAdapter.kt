package net.daverix.urlforward

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import net.daverix.urlforward.databinding.FilterRowBinding


class FiltersAdapter : ListAdapter<FilterRowViewModel, FiltersAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<FilterRowBinding>(inflater,
                R.layout.filter_row,
                parent,
                false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.filter = getItem(position)
        holder.binding.executePendingBindings()
    }

    class ViewHolder(val binding: FilterRowBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FilterRowViewModel>() {
            override fun areItemsTheSame(oldItem: FilterRowViewModel, newItem: FilterRowViewModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: FilterRowViewModel, newItem: FilterRowViewModel): Boolean =
                    oldItem.id == newItem.id &&
                            oldItem.title == newItem.title &&
                            oldItem.filterUrl == newItem.filterUrl
        }
    }
}

@BindingAdapter("items")
fun RecyclerView.bindItems(items: List<FilterRowViewModel>?) {
    if(adapter == null) {
        adapter = FiltersAdapter()
    }

    (adapter as FiltersAdapter).submitList(items)
}
