/*
    UrlForwarder makes it possible to use bookmarklets on Android
    Copyright (C) 2017 David Laurell

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


import android.arch.persistence.room.*
import android.provider.BaseColumns
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface LinkFilterDao {
    @Insert
    fun insertSync(linkFilter: LinkFilter): Long

    @Update
    fun updateSync(linkFilter: LinkFilter): Int

    @Delete
    fun deleteSync(linkFilter: LinkFilter): Int

    @Query("SELECT * FROM $TABLE_FILTER WHERE ${BaseColumns._ID} = :id LIMIT 1")
    fun getFilterSync(id: Long): LinkFilter

    @Query("SELECT * FROM $TABLE_FILTER")
    fun queryAllSync(): List<LinkFilter>
}

fun LinkFilterDao.queryAll(): Single<List<LinkFilter>> {
    return Single.create {
        it.onSuccess(queryAllSync())
    }
}

fun LinkFilterDao.getFilter(id: Long): Single<LinkFilter> {
    return Single.create {
        it.onSuccess(getFilterSync(id))
    }
}

fun LinkFilterDao.insert(linkFilter: LinkFilter): Completable {
    return Completable.create {
        val id = insertSync(linkFilter)
        if(id > 0) {
            linkFilter.id = id
            it.onComplete()
        } else {
            it.onError(IllegalStateException("filter not inserted"))
        }
    }
}

fun LinkFilterDao.update(linkFilter: LinkFilter): Completable {
    return Completable.create {
        val updated = updateSync(linkFilter)
        if(updated > 0) {
            it.onComplete()
        } else {
            it.onError(IllegalStateException("filter not updated"))
        }
    }
}

fun LinkFilterDao.delete(linkFilter: LinkFilter): Completable {
    return Completable.create {
        val deleted = deleteSync(linkFilter)
        if(deleted > 0) {
            it.onComplete()
        } else {
            it.onError(IllegalStateException("filter not deleted"))
        }
    }
}
