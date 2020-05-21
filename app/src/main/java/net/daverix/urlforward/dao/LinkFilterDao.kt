/*
    UrlForwarder makes it possible to use bookmarklets on Android
    Copyright (C) 2018 David Laurell

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.daverix.urlforward.dao


import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface LinkFilterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(linkFilter: LinkFilter): Long

    @Delete
    suspend fun delete(linkFilter: LinkFilter): Int

    @Query("SELECT * FROM filter WHERE _id = :id LIMIT 1")
    suspend fun getFilter(id: Long): LinkFilter?

    @Query("SELECT * FROM filter")
    fun getFilters(): Flow<List<LinkFilter>>
}
