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
package net.daverix.urlforward

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import net.daverix.urlforward.db.FILTER_MIME_TYPE_DIR
import net.daverix.urlforward.db.FILTER_MIME_TYPE_ITEM

class UrlForwarderProvider : ContentProvider() {
    override fun onCreate(): Boolean {
        return true
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        return null
    }

    override fun getType(uri: Uri): String? {
        val match = sUriMatcher.match(uri)
        when (match) {
            MATCH_FILTERS -> return FILTER_MIME_TYPE_DIR
            MATCH_FILTER -> return FILTER_MIME_TYPE_ITEM
            else -> return null
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }

    companion object {
        private val sUriMatcher = UriMatcher(0)
        private val MATCH_FILTER = 1
        private val MATCH_FILTERS = 2

        init {
            sUriMatcher.addURI(AUTHORITY, "filter/#", MATCH_FILTER)
            sUriMatcher.addURI(AUTHORITY, "filter", MATCH_FILTERS)
        }
    }
}
