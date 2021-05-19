package net.daverix.urlforward

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class FiltersViewModel : ViewModel() {
    var filters: List<LinkFilter> by mutableStateOf(listOf())
        private set

    fun addItem() {
        filters = filters.toMutableList().apply {
            add(
                LinkFilter(
                    title = "test",
                    outputUrl = "http://example.com",
                    urlPattern = "asdasdas",
                    subjectPattern = "asdfadfgsdf"
                )
            )
        }
    }

    fun editItem(filter: LinkFilter) {

    }
}