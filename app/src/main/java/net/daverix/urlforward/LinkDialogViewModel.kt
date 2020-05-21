package net.daverix.urlforward

import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import net.daverix.urlforward.dao.LinkFilterDao
import java.net.URLEncoder


class LinkDialogViewModel @AssistedInject constructor(
        private val linkFilterDao: LinkFilterDao,
        @Assisted private val url: String,
        @Assisted private val subject: String
) : ViewModel() {
    val events = Channel<Event>()

    val items: LiveData<List<LinkRowViewModel>> = liveData {
        linkFilterDao.getFilters()
                .map { filters ->
                    filters.map { filter ->
                        LinkRowViewModel(filter.id, filter.title) {
                            viewModelScope.launch {
                                val replacement = if (!filter.skipEncode) URLEncoder.encode(url, "UTF-8") else url
                                var filteredUrl = filter.filterUrl.replace(filter.replaceText, replacement)

                                filteredUrl = filteredUrl.replace(filter.replaceSubject, subject)
                                val url = filteredUrl.toUri()
                                events.send(OpenLink(url))
                            }
                        }
                    }
                }
                .collect { linkFilters ->
                    emit(linkFilters)
                }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(url: String, subject: String): LinkDialogViewModel
    }
}