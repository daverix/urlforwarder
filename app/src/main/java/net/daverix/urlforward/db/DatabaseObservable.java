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
package net.daverix.urlforward.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Function;

@Singleton
public class DatabaseObservable {
    private final Provider<SQLiteDatabase> databaseProvider;

    @Inject
    public DatabaseObservable(Provider<SQLiteDatabase> databaseProvider) {
        this.databaseProvider = databaseProvider;
    }

    private Single<SQLiteDatabase> getDatabase() {
        return Single.create(s -> {
            if(s.isDisposed()) return;

            SQLiteDatabase db = databaseProvider.get();
            if(!s.isDisposed())
                s.onSuccess(db);
        });
    }

    public Single<Long> insert(String table, ContentValues values) {
        return getDatabase().map(db -> db.insertOrThrow(table, null, values));
    }

    public Single<Integer> update(String table, ContentValues values, String where, String[] whereArgs) {
        return getDatabase().map(db -> db.update(table, values, where, whereArgs));
    }

    public Single<Integer> delete(String table, String where, String[] whereArgs) {
        return getDatabase().map(db -> db.delete(table, where, whereArgs));
    }

    public Observable<Cursor> query(String table, String[] columns, String selection, String[] selectionArgs, String limit) {
        return query(db -> db.query(false, table, columns, selection, selectionArgs, null, null, null, limit));
    }

    private Observable<Cursor> query(Function<SQLiteDatabase,Cursor> queryFunc) {
        return Observable.create(s -> {
            if(s.isDisposed())
                return;

            Cursor cursor = null;
            try {
                SQLiteDatabase db = databaseProvider.get();
                cursor = queryFunc.apply(db);

                if (s.isDisposed())
                    return;

                if (cursor == null) {
                    s.onError(new IllegalStateException("cursor is null"));
                    return;
                }

                while (!s.isDisposed() && cursor.moveToNext()) {
                    s.onNext(cursor);
                }

                if (!s.isDisposed()) {
                    s.onComplete();
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        });
    }
}
