package net.daverix.urlforward.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import net.daverix.urlforward.Constants;

import static net.daverix.urlforward.db.UrlForwarderContract.UrlFilters;

/**
 * Created by daverix on 12/25/13.
 */
public class UrlForwarderProvider extends ContentProvider {
    private static UriMatcher sUriMatcher = new UriMatcher(0);
    private static final int MATCH_FILTER = 1;
    private static final int MATCH_FILTERS = 2;
    static {
        sUriMatcher.addURI(Constants.AUTHORITY, "filter/#", MATCH_FILTER);
        sUriMatcher.addURI(Constants.AUTHORITY, "filter", MATCH_FILTERS);
    }

    private UrlForwardDatabaseHelper mHelper;

    @Override
    public boolean onCreate() {
        mHelper = new UrlForwardDatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int match = sUriMatcher.match(uri);

        if(match == 0)
            return null;

        SQLiteDatabase db = mHelper.getReadableDatabase();
        if(db == null)
            return null;

        Cursor cursor = null;
        switch (match) {
            case MATCH_FILTER:
                cursor = db.query(false, UrlForwardDatabaseHelper.TABLE_FILTER, projection, UrlFilters._ID + "=?", new String[]{uri.getLastPathSegment()}, null, null, sortOrder, null);
                break;
            case MATCH_FILTERS:
                cursor = db.query(false, UrlForwardDatabaseHelper.TABLE_FILTER, projection, selection, selectionArgs, null, null, sortOrder, null);
                break;
        }

        if(cursor != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
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
    public Uri insert(Uri uri, ContentValues values) {
        int match = sUriMatcher.match(uri);

        if(match == 0)
            return null;

        SQLiteDatabase db = mHelper.getWritableDatabase();
        if(db == null)
            return null;

        switch (match) {
            case MATCH_FILTERS:
                long id = db.insert(UrlForwardDatabaseHelper.TABLE_FILTER, null, values);
                Uri insertedUri = null;
                if(id > 0) {
                    insertedUri = Uri.withAppendedPath(UrlFilters.CONTENT_URI, String.valueOf(id));
                    getContext().getContentResolver().notifyChange(insertedUri, null);
                }
                return insertedUri;
            default:
                return null;
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int match = sUriMatcher.match(uri);

        if(match == 0)
            return -1;

        SQLiteDatabase db = mHelper.getWritableDatabase();
        if(db == null)
            return -1;

        int deleted = 0;
        switch (match) {
            case MATCH_FILTER:
                deleted = db.delete(UrlForwardDatabaseHelper.TABLE_FILTER, UrlFilters._ID + "=?", new String[] {uri.getLastPathSegment()});
                break;
            case MATCH_FILTERS:
                deleted = db.delete(UrlForwardDatabaseHelper.TABLE_FILTER, selection, selectionArgs);
                break;
        }

        if(deleted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return deleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int match = sUriMatcher.match(uri);

        if(match == 0)
            return -1;

        SQLiteDatabase db = mHelper.getWritableDatabase();
        if(db == null)
            return -1;

        int updated = 0;
        switch (match) {
            case MATCH_FILTER:
                updated = db.update(UrlForwardDatabaseHelper.TABLE_FILTER, values, UrlFilters._ID + "=?", new String[] {uri.getLastPathSegment()});
                break;
            case MATCH_FILTERS:
                updated = db.update(UrlForwardDatabaseHelper.TABLE_FILTER, values, selection, selectionArgs);
                break;
        }

        if(updated > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return updated;
    }
}
