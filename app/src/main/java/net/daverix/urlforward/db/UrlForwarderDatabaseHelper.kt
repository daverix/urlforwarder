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
import android.provider.BaseColumns._ID
import net.daverix.urlforward.TABLE_FILTER
import javax.inject.Inject
import javax.inject.Singleton

private val DB_NAME = "UrlForward"
private val DB_VERSION = 4

@Singleton
class UrlForwarderDatabaseHelper @Inject constructor(context: Context) :
        SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE IF NOT EXISTS $TABLE_FILTER(" +
                "$_ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "$FILTER_TITLE TEXT NOT NULL," +
                "$FILTER_URL TEXT NOT NULL," +
                "$FILTER_REPLACE_TEXT TEXT NOT NULL," +
                "$FILTER_CREATED INTEGER NOT NULL," +
                "$FILTER_UPDATED INTEGER NOT NULL," +
                "$FILTER_SKIP_ENCODE INTEGER DEFAULT 0," +
                "$FILTER_REPLACE_SUBJECT TEXT DEFAULT '')")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 3) {
            db.execSQL("ALTER TABLE $TABLE_FILTER ADD COLUMN $FILTER_SKIP_ENCODE INTEGER DEFAULT 0")
        }

        if (oldVersion < 4) {
            db.execSQL("ALTER TABLE $TABLE_FILTER ADD COLUMN $FILTER_REPLACE_SUBJECT TEXT DEFAULT ''")
        }
    }
}
