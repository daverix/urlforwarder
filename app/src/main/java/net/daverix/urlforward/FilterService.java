package net.daverix.urlforward;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import net.daverix.urlforward.db.UrlForwarderContract;

/**
 * Created by daverix on 12/29/13.
 */
public class FilterService extends IntentService {
    public static final String EXTRA_LINK_FILTER = "linkFilter";

    public FilterService() {
        super("FilterService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        LinkFilter filter = intent.getParcelableExtra(EXTRA_LINK_FILTER);

        if(Intent.ACTION_INSERT.equals(intent.getAction())) {
            insertLinkFilter(filter);
        } else if(Intent.ACTION_EDIT.equals(intent.getAction())) {
            updateLinkFilter(intent.getData(), filter);
        } else if(Intent.ACTION_DELETE.equals(intent.getAction())) {
            deleteLinkFilter(intent.getData());
        }
    }

    private void insertLinkFilter(LinkFilter linkFilter) {
        Uri uri = getContentResolver().insert(UrlForwarderContract.UrlFilters.CONTENT_URI, getValues(linkFilter));
        if(uri != null) {
            Log.d("FilterService", "Inserted " + uri);
        }
        else {
            Log.e("FilterService", "Error inserting filter");
        }
    }

    private void updateLinkFilter(Uri uri, LinkFilter linkFilter) {
        int updated = getContentResolver().update(uri, getValues(linkFilter), null, null);
        if(updated > 0) {
            Log.d("FilterService", "Updated " + uri);
        }
        else {
            Log.e("FilterService", "Error updating filter");
        }
    }

    private void deleteLinkFilter(Uri uri) {
        int deleted = getContentResolver().delete(uri, null, null);
        if(deleted > 0) {
            Log.d("FilterService", "Deleted " + uri);
        }
        else {
            Log.e("FilterService", "Error deleting filter " + uri);
        }
    }

    private ContentValues getValues(LinkFilter filter) {
        ContentValues values = new ContentValues();
        values.put(UrlForwarderContract.UrlFilters.CREATED, filter.getCreated());
        values.put(UrlForwarderContract.UrlFilters.UPDATED, filter.getUpdated());
        values.put(UrlForwarderContract.UrlFilters.TITLE, filter.getTitle());
        values.put(UrlForwarderContract.UrlFilters.FILTER, filter.getFilterUrl());
        values.put(UrlForwarderContract.UrlFilters.REPLACE_TEXT, filter.getReplaceText());

        return values;
    }
}
