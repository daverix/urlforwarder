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

import android.provider.BaseColumns
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

const val FILTER_TITLE = "title"
const val FILTER_URL = "url"
const val FILTER_REPLACE_TEXT = "replace_text"
const val FILTER_CREATED = "created"
const val FILTER_UPDATED = "updated"
const val FILTER_SKIP_ENCODE = "skipEncode"
const val FILTER_REPLACE_SUBJECT = "replace_subject"
const val TABLE_FILTER = "filter"

@Entity(tableName =  TABLE_FILTER)
data class LinkFilter(@PrimaryKey(autoGenerate = true)
                      @ColumnInfo(name = BaseColumns._ID)
                      var id: Long,

                      @ColumnInfo(name = FILTER_TITLE)
                      var title: String = "",

                      @ColumnInfo(name = FILTER_URL)
                      var filterUrl: String,

                      @ColumnInfo(name = FILTER_REPLACE_TEXT)
                      var replaceText: String,

                      @ColumnInfo(name = FILTER_REPLACE_SUBJECT)
                      var replaceSubject: String,

                      @ColumnInfo(name = FILTER_CREATED)
                      var created: Date,

                      @ColumnInfo(name = FILTER_UPDATED)
                      var updated: Date,

                      @ColumnInfo(name = FILTER_SKIP_ENCODE)
                      var skipEncode: Boolean)
