package net.daverix.urlforward

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import net.daverix.urlforward.databinding.LinkRowBinding

class LinksAdapter : ListAdapter<LinkRowViewModel, LinksAdapter.ViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(DataBindingUtil.inflate(inflater, R.layout.link_row, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.filter = getItem(position)
        holder.binding.executePendingBindings()
    }

    class ViewHolder(val binding: LinkRowBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<LinkRowViewModel>() {
            override fun areItemsTheSame(oldItem: LinkRowViewModel, newItem: LinkRowViewModel): Boolean =
                    oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: LinkRowViewModel, newItem: LinkRowViewModel): Boolean =
                    oldItem.id == newItem.id &&
                            oldItem.title == newItem.title
        }
    }
}

@BindingAdapter("items")
fun RecyclerView.bindItems(items: List<LinkRowViewModel>?) {
    if(adapter == null) {
        adapter = LinksAdapter()
    }

    (adapter as LinksAdapter).submitList(items)
}