package net.daverix.urlforward

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import net.daverix.urlforward.db.FilterDao

class FakeFilterDao : FilterDao {
    private val filters = MutableStateFlow<List<LinkFilter>>(emptyList())

    override suspend fun insert(filter: LinkFilter) {
        filters.value = filters.value + filter
    }

    override suspend fun update(filter: LinkFilter) {
        val index = filters.value.indexOfFirst { it.id == filter.id }
        if(index == -1)
            error("cannot find ${filter.id}")

        filters.value = filters.value.toMutableList().apply {
            this[index] = filter
        }
    }

    override suspend fun delete(filterId: Long) {
        filters.value = filters.value.filter { it.id != filterId }
    }

    override fun queryFilters(): Flow<List<LinkFilter>> = filters
    override fun queryRegexFilters(): Flow<List<LinkFilter>> {
        TODO("Not yet implemented")
    }

    override fun queryAllRegexFilters(): List<LinkFilter> {
        TODO("Not yet implemented")
    }

    override suspend fun queryFilter(filterId: Long): LinkFilter? =
        filters.value.firstOrNull { it.id == filterId }
}