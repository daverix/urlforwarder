package net.daverix.urlforward

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import net.daverix.urlforward.databinding.FilterRowBinding
import java.util.*

class FilterAdapter internal constructor(private val filtersFragment: FiltersFragment) : RecyclerView.Adapter<BindingHolder<FilterRowBinding>>() {
    var filters: List<FilterRowViewModel> = ArrayList()

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): BindingHolder<FilterRowBinding> {
        val binding = DataBindingUtil.inflate<FilterRowBinding>(LayoutInflater.from(filtersFragment.activity),
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