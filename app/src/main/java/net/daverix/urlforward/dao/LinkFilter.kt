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

@Entity(tableName = "filter")
data class LinkFilter(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = BaseColumns._ID)
        val id: Long = 0,

        @ColumnInfo(name = FILTER_TITLE)
        val title: String = "",

        @ColumnInfo(name = FILTER_URL)
        val filterUrl: String,

        @ColumnInfo(name = FILTER_REPLACE_TEXT)
        val replaceText: String,

        @ColumnInfo(name = FILTER_REPLACE_SUBJECT)
        val replaceSubject: String,

        @ColumnInfo(name = FILTER_CREATED)
        val created: Long,

        @ColumnInfo(name = FILTER_UPDATED)
        val updated: Long,

        @ColumnInfo(name = FILTER_SKIP_ENCODE)
        val skipEncode: Boolean
)
