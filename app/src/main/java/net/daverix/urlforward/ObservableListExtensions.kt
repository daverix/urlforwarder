package net.daverix.urlforward

import android.databinding.ObservableList

fun <T,U> ObservableList<T>.updateList(items: List<U>,
                                       equal: (U, T) -> Boolean,
                                       transform: (U) -> T) {
    forEachIndexed { filterIndex, filter ->
        val index = items.indexOfFirst { item -> equal(item, filter) }
        if (index == -1) {
            removeAt(filterIndex)
        } else {
            this[filterIndex] = transform(items[index])
        }
    }

    items.forEach { item ->
        if (indexOfFirst { equal(item, it) } == -1) {
            add(transform(item))
        }
    }
}
