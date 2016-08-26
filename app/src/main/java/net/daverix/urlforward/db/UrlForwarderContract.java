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
package net.daverix.urlforward.db;

import android.net.Uri;
import android.provider.BaseColumns;

public final class UrlForwarderContract {
    protected interface UrlFilterColumns {
        String TITLE = "title";
        String FILTER = "url";
        String REPLACE_TEXT = "replace_text";
        String CREATED = "created";
        String UPDATED = "updated";
        String SKIP_ENCODE = "skipEncode";
    }

    public static class UrlFilters implements BaseColumns, UrlFilterColumns {
        public static final Uri CONTENT_URI = Uri.parse("content://net.daverix.urlforward.provider/filter");
        public static final String MIME_TYPE_DIR = "vnd.android.cursor.dir/vnd.net.daverix.provider.filter";
        public static final String MIME_TYPE_ITEM = "vnd.android.cursor.item/vnd.net.daverix.provider.filter";

    }
}
