package net.daverix.urlforward.db;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by daverix on 12/25/13.
 */
public final class UrlForwarderContract {
    protected interface UrlFilterColumns {
        public static final String TITLE = "title";
        public static final String FILTER = "url";
        public static final String REPLACE_TEXT = "replace_text";
        public static final String CREATED = "created";
        public static final String UPDATED = "updated";
    }

    public static class UrlFilters implements BaseColumns, UrlFilterColumns {
        public static final Uri CONTENT_URI = Uri.parse("content://net.daverix.urlforward.provider/filter");
        public static final String MIME_TYPE_DIR = "vnd.android.cursor.dir/vnd.net.daverix.provider.filter";
        public static final String MIME_TYPE_ITEM = "vnd.android.cursor.item/vnd.net.daverix.provider.filter";
    }
}
