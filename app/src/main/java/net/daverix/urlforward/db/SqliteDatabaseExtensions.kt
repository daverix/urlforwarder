package net.daverix.urlforward.db

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Build
import android.os.CancellationSignal
import io.reactivex.Observable
import io.reactivex.ObservableEmitter

fun SQLiteDatabase.query(table: String,
                         columns: Array<String>? = null,
                         selection: String? = null,
                         selectionArgs: Array<String>? = null,
                         orderBy: String?,
                         limit: String? = null): Observable<Cursor> {
    fun queryWithSignal(s: ObservableEmitter<Cursor>): Cursor {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            val cancellationSignal = CancellationSignal()
            s.setCancellable {
                cancellationSignal.cancel()
            }

            return query(false, table, columns, selection, selectionArgs, null, null, orderBy,
                    limit, cancellationSignal)
        } else {
            return query(false, table, columns, selection, selectionArgs, null, null, orderBy,
                    limit)
        }
    }

    return Observable.create<Cursor> { s ->
        queryWithSignal(s).use {
            if (s.isDisposed)
                return@create

            while (!s.isDisposed && it.moveToNext()) {
                s.onNext(it)
            }
        }

        if (!s.isDisposed) {
            s.onComplete()
        }
    }
}
