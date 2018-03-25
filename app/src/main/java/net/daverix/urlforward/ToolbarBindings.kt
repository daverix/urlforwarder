package net.daverix.urlforward

import android.databinding.BindingAdapter
import android.support.annotation.MenuRes
import android.support.v7.widget.Toolbar
import android.support.v7.widget.Toolbar.OnMenuItemClickListener
import android.view.View

@BindingAdapter("onNavigationClick")
fun Toolbar.bindNavigationClick(listener: View.OnClickListener) {
    setNavigationOnClickListener(listener)
}

@BindingAdapter("menu")
fun Toolbar.bindMenu(@MenuRes menuId: Int) = inflateMenu(menuId)

@BindingAdapter("onMenuItemClick")
fun Toolbar.bindMenuClick(listener: OnMenuItemClickListener) {
    setOnMenuItemClickListener(listener)
}
