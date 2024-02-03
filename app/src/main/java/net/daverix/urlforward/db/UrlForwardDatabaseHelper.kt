/*
    UrlForwarder makes it possible to use bookmarklets on Android
    Copyright (C) 2016 David Laurell

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

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import net.daverix.urlforward.db.UrlForwarderContract.UrlFilterColumns.CREATED
import net.daverix.urlforward.db.UrlForwarderContract.UrlFilterColumns.FILTER
import net.daverix.urlforward.db.UrlForwarderContract.UrlFilterColumns.REGEX_PATTERN
import net.daverix.urlforward.db.UrlForwarderContract.UrlFilterColumns.REPLACE_SUBJECT
import net.daverix.urlforward.db.UrlForwarderContract.UrlFilterColumns.REPLACE_TEXT
import net.daverix.urlforward.db.UrlForwarderContract.UrlFilterColumns.SKIP_ENCODE
import net.daverix.urlforward.db.UrlForwarderContract.UrlFilterColumns.TITLE
import net.daverix.urlforward.db.UrlForwarderContract.UrlFilterColumns.UPDATED

class UrlForwardDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_FILTER)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 3) {
            db.execSQL("ALTER TABLE $TABLE_FILTER ADD COLUMN $SKIP_ENCODE INTEGER DEFAULT 0")
        }
        if (oldVersion < 4) {
            db.execSQL("ALTER TABLE $TABLE_FILTER ADD COLUMN $REPLACE_SUBJECT TEXT DEFAULT ''")
        }
        if (oldVersion < 5) {
            db.execSQL("ALTER TABLE $TABLE_FILTER ADD COLUMN $REGEX_PATTERN TEXT DEFAULT ''")
        }
    }

    fun writableTransaction(func: SQLiteDatabase.()->Unit) {
        writableDatabase.use {
            try {
                it.beginTransaction()
                func(it)
                it.setTransactionSuccessful()
            } finally {
                it.endTransaction()
            }
        }
    }

    companion object {
        private const val DB_NAME = "UrlForward"
        private const val DB_VERSION = 4

        const val TABLE_FILTER = "filter"

        private const val CREATE_FILTER = """
            CREATE TABLE IF NOT EXISTS $TABLE_FILTER(
                ${BaseColumns._ID} INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                $TITLE TEXT NOT NULL,
                $FILTER TEXT NOT NULL,
                $REPLACE_TEXT TEXT NOT NULL,
                $CREATED INTEGER NOT NULL,
                $UPDATED INTEGER NOT NULL,
                $SKIP_ENCODE INTEGER DEFAULT 0,
                $REPLACE_SUBJECT TEXT DEFAULT '',
                $REGEX_PATTERN TEXT DEFAULT ''
                
            )
            """
    }
}
