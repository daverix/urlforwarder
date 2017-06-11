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
package net.daverix.urlforward.db


import android.provider.BaseColumns._ID
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import net.daverix.urlforward.LinkFilter
import net.daverix.urlforward.TABLE_FILTER
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Provider

class LinkFilterStorageImpl @Inject
constructor(private val database: DatabaseObservable,
            private val mapper: Mapper<LinkFilter>,
            @param:Named("timestamp") private val timestampProvider: Provider<Long>) : LinkFilterStorage {

    override fun insert(linkFilter: LinkFilter): Completable {
        return Single.defer {
            linkFilter.updated = timestampProvider.get()
            database.insert(TABLE_FILTER, mapper.getValues(linkFilter))
        }.doAfterSuccess {
            linkFilter.id = it
        }.toCompletable()
    }

    override fun update(linkFilter: LinkFilter): Completable {
        linkFilter.updated = timestampProvider.get()
        return Observable.just(linkFilter)
                .map(mapper::getValues)
                .flatMapCompletable { values ->
                    val args = getIdArgs(linkFilter.id)
                    database.update(TABLE_FILTER, values, _ID + "=?", args)
                            .toCompletable()
                }
    }

    override fun delete(id: Long): Completable {
        val args = getIdArgs(id)
        return Completable.defer {
            database.delete(TABLE_FILTER, _ID + "=?", args)
                    .toCompletable()
        }
    }

    private fun getIdArgs(id: Long): Array<String> {
        return arrayOf(id.toString())
    }

    override fun getFilter(id: Long): Single<LinkFilter> {
        val query = String.format("%s=?", _ID)
        val columns = mapper.columns
        val args = getIdArgs(id)

        return database.query(TABLE_FILTER, columns, query, args, "1")
                .map(mapper::mapFilter)
                .singleOrError()
    }

    override fun queryAll(): Observable<LinkFilter> {
        return database.query(TABLE_FILTER, mapper.columns).map(mapper::mapFilter)
    }
}
