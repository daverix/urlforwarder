package net.daverix.urlforward

import android.view.LayoutInflater
import android.view.ViewGroup
import net.daverix.urlforward.databinding.FilterRowBinding


class FilterRowBinder(private val inflater: LayoutInflater) : Binder<FilterRowBinding, FilterRowViewModel> {
    override fun bind(binding: FilterRowBinding, item: FilterRowViewModel) {
        binding.filter = item
    }

    override fun createBinding(viewGroup: ViewGroup, viewType: Int): FilterRowBinding {
        return FilterRowBinding.inflate(inflater, viewGroup, false)
    }
}