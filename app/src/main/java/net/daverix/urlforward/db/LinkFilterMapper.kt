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
import android.provider.BaseColumns

import net.daverix.urlforward.LinkFilter

import javax.inject.Inject

class LinkFilterMapper @Inject
constructor() : Mapper<LinkFilter> {

    override val columns: Array<String>
        get() = arrayOf(BaseColumns._ID,
                FILTER_TITLE,
                FILTER_URL,
                FILTER_REPLACE_TEXT,
                FILTER_CREATED,
                FILTER_UPDATED,
                FILTER_SKIP_ENCODE,
                FILTER_REPLACE_SUBJECT)

    override fun mapFilter(cursor: Cursor): LinkFilter {
        val filter = LinkFilter()
        filter.id = cursor.getLong(0)
        filter.title = cursor.getString(1)
        filter.filterUrl = cursor.getString(2)
        filter.replaceText = cursor.getString(3)
        filter.created = cursor.getLong(4)
        filter.updated = cursor.getLong(5)
        filter.encoded = cursor.getShort(6).toInt() != 1
        filter.replaceSubject = cursor.getString(7)
        return filter
    }

    override fun getValues(filter: LinkFilter): ContentValues {
        val values = ContentValues()
        values.put(FILTER_CREATED, filter.created)
        values.put(FILTER_UPDATED, filter.updated)
        values.put(FILTER_TITLE, filter.title)
        values.put(FILTER_URL, filter.filterUrl)
        values.put(FILTER_REPLACE_TEXT, filter.replaceText)
        values.put(FILTER_SKIP_ENCODE, !filter.encoded)
        values.put(FILTER_REPLACE_SUBJECT, filter.replaceSubject)
        return values
    }
}
