package net.daverix.urlforward.view

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("visibilityGone")
fun View.visibilityGone(gone: Boolean) {
    visibility = if(gone) View.GONE else View.VISIBLE
}
