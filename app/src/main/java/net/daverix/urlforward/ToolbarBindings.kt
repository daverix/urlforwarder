package net.daverix.urlforward

import android.databinding.BindingAdapter
import android.support.v7.widget.Toolbar
import android.support.v7.widget.Toolbar.OnMenuItemClickListener
import android.view.View

object ToolbarBindings {
    @JvmStatic @BindingAdapter("onNavigationClick")
    fun bindNavigationClick(toolbar: Toolbar, listener: View.OnClickListener) {
        toolbar.setNavigationOnClickListener(listener)
    }

    @JvmStatic @BindingAdapter("menu")
    fun bindMenu(toolbar: Toolbar, menuId: Int) {
        toolbar.inflateMenu(menuId)
    }

    @JvmStatic @BindingAdapter("onMenuItemClick")
    fun bindMenuClick(toolbar: Toolbar, listener: OnMenuItemClickListener?) {
        toolbar.setOnMenuItemClickListener(listener)
    }
}