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

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.provider.BaseColumns
import net.daverix.urlforward.Constants
import net.daverix.urlforward.db.UrlForwarderContract.UrlFilters

class UrlForwarderProvider : ContentProvider() {
    companion object {
        private val sUriMatcher = UriMatcher(0)
        private const val MATCH_FILTER = 1
        private const val MATCH_FILTERS = 2

        init {
            sUriMatcher.addURI(Constants.AUTHORITY, "filter/#", MATCH_FILTER)
            sUriMatcher.addURI(Constants.AUTHORITY, "filter", MATCH_FILTERS)
        }
    }

    private lateinit var dbHelper: UrlForwardDatabaseHelper
    override fun onCreate(): Boolean {
        dbHelper = UrlForwardDatabaseHelper(context!!)
        return true
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        val match = sUriMatcher.match(uri)
        if (match == 0) return null
        val db = dbHelper.readableDatabase ?: return null
        var cursor: Cursor? = null
        when (match) {
            MATCH_FILTER -> cursor = db.query(false, UrlForwardDatabaseHelper.TABLE_FILTER, projection, BaseColumns._ID + "=?", arrayOf(uri.lastPathSegment), null, null, sortOrder, null)
            MATCH_FILTERS -> cursor = db.query(false, UrlForwardDatabaseHelper.TABLE_FILTER, projection, selection, selectionArgs, null, null, sortOrder, null)
        }
        cursor?.setNotificationUri(context!!.contentResolver, uri)
        return cursor
    }

    override fun getType(uri: Uri): String? {
        return when (sUriMatcher.match(uri)) {
            MATCH_FILTERS -> UrlFilters.MIME_TYPE_DIR
            MATCH_FILTER -> UrlFilters.MIME_TYPE_ITEM
            else -> null
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val match = sUriMatcher.match(uri)
        if (match == 0) return null
        val db = dbHelper.writableDatabase ?: return null
        return when (match) {
            MATCH_FILTERS -> {
                val id = db.insert(UrlForwardDatabaseHelper.TABLE_FILTER, null, values)
                var insertedUri: Uri? = null
                if (id > 0) {
                    insertedUri = Uri.withAppendedPath(UrlFilters.CONTENT_URI, id.toString())
                    context!!.contentResolver.notifyChange(insertedUri, null)
                }
                insertedUri
            }
            else -> null
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val match = sUriMatcher.match(uri)
        if (match == 0) return -1
        val db = dbHelper.writableDatabase ?: return -1
        var deleted = 0
        when (match) {
            MATCH_FILTER -> deleted = db.delete(UrlForwardDatabaseHelper.TABLE_FILTER, BaseColumns._ID + "=?", arrayOf(uri.lastPathSegment))
            MATCH_FILTERS -> deleted = db.delete(UrlForwardDatabaseHelper.TABLE_FILTER, selection, selectionArgs)
        }
        if (deleted > 0) {
            context!!.contentResolver.notifyChange(uri, null)
        }
        return deleted
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        val match = sUriMatcher.match(uri)
        if (match == 0) return -1
        val db = dbHelper.writableDatabase ?: return -1
        var updated = 0
        when (match) {
            MATCH_FILTER -> updated = db.update(UrlForwardDatabaseHelper.TABLE_FILTER, values, BaseColumns._ID + "=?", arrayOf(uri.lastPathSegment))
            MATCH_FILTERS -> updated = db.update(UrlForwardDatabaseHelper.TABLE_FILTER, values, selection, selectionArgs)
        }
        if (updated > 0) {
            context!!.contentResolver.notifyChange(uri, null)
        }
        return updated
    }
}