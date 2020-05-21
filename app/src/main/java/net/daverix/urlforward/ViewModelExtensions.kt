package net.daverix.urlforward

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

fun <T> factory(func: ()->T) = object : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return func() as T
    }
}
