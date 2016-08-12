package net.daverix.urlforward;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

class BindingHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder {
    final T binding;

    BindingHolder(T binding) {
        super(binding.getRoot());

        this.binding = binding;
    }
}
