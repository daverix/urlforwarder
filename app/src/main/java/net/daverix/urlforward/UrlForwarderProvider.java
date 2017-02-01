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
package net.daverix.urlforward;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import net.daverix.urlforward.db.UrlForwarderDatabaseHelper;

import static net.daverix.urlforward.db.UrlForwarderContract.UrlFilters;

public class UrlForwarderProvider extends ContentProvider {
    private static UriMatcher sUriMatcher = new UriMatcher(0);
    private static final int MATCH_FILTER = 1;
    private static final int MATCH_FILTERS = 2;
    static {
        sUriMatcher.addURI(Constants.AUTHORITY, "filter/#", MATCH_FILTER);
        sUriMatcher.addURI(Constants.AUTHORITY, "filter", MATCH_FILTERS);
    }

    private UrlForwarderDatabaseHelper mHelper;

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        int match = sUriMatcher.match(uri);
        switch (match) {
            case MATCH_FILTERS:
                return UrlFilters.MIME_TYPE_DIR;
            case MATCH_FILTER:
                return UrlFilters.MIME_TYPE_ITEM;
            default:
                return null;
        }
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
