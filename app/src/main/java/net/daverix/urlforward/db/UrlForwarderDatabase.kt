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

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.arch.persistence.room.migration.Migration
import net.daverix.urlforward.dao.*

const val DB_NAME = "UrlForward"
const val DB_VERSION = 4

@Database(entities = arrayOf(LinkFilter::class), version = DB_VERSION, exportSchema = true)
@TypeConverters(DbTypeConverters::class)
abstract class UrlForwarderDatabase : RoomDatabase() {
    abstract fun getLinkFilterDao(): LinkFilterDao
}

class MigrationTo3 : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase?) {
        db?.execSQL("ALTER TABLE $TABLE_FILTER ADD COLUMN $FILTER_SKIP_ENCODE INTEGER DEFAULT 0")
    }
}

class MigrationTo4 : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase?) {
        db?.execSQL("ALTER TABLE $TABLE_FILTER ADD COLUMN $FILTER_REPLACE_SUBJECT TEXT DEFAULT ''")
    }

}
