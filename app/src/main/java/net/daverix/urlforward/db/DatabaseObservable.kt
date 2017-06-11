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

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class DatabaseObservable @Inject
constructor(private val databaseProvider: Provider<SQLiteDatabase>) {
    fun insert(table: String, values: ContentValues): Single<Long> {
        return Single.create { s ->
            s.onSuccess(databaseProvider.get().insertOrThrow(table, null, values))
        }
    }

    fun update(table: String, values: ContentValues, where: String, whereArgs: Array<String>): Single<Int> {
        return Single.create { s ->
            s.onSuccess(databaseProvider.get().update(table, values, where, whereArgs))
        }
    }

    fun delete(table: String, where: String?, whereArgs: Array<String>?): Single<Int> {
        return Single.create { s ->
            s.onSuccess(databaseProvider.get().delete(table, where, whereArgs))
        }
    }

    fun query(table: String,
              columns: Array<String>? = null,
              selection: String? = null,
              selectionArgs: Array<String>? = null,
              orderBy: String? = null,
              limit: String? = null): Observable<Cursor> {
        return Observable.defer {
            databaseProvider.get().query(table, columns, selection, selectionArgs, orderBy, limit)
        }
    }
}
