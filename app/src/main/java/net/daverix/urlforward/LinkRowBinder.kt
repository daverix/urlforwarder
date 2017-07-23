package net.daverix.urlforward

import android.view.LayoutInflater
import android.view.ViewGroup
import net.daverix.urlforward.databinding.LinkRowBinding


class LinkRowBinder(private val inflater: LayoutInflater) : Binder<LinkRowBinding, LinkRowViewModel> {
    override fun bind(binding: LinkRowBinding, item: LinkRowViewModel) {
        binding.filter = item
    }

    override fun createBinding(viewGroup: ViewGroup, viewType: Int): LinkRowBinding {
        return LinkRowBinding.inflate(inflater, viewGroup, false)
    }
}