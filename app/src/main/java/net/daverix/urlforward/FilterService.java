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

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import net.daverix.urlforward.db.UrlForwarderContract;

public class FilterService extends IntentService {
    public static final String EXTRA_LINK_FILTER = "linkFilter";
    private LinkFilterMapper mapper;

    public FilterService() {
        super("FilterService");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mapper = new LinkFilterMapperImpl();
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

        if(BuildConfig.DEBUG) {
            ModifyFilterIdlingResource resource = ((UrlForwarderApplication) getApplication())
                    .getModifyFilterIdlingResource();
            if(resource != null)
                resource.setIdle(true);
        }
    }

    private void insertLinkFilter(LinkFilter linkFilter) {
        Uri uri = getContentResolver().insert(UrlForwarderContract.UrlFilters.CONTENT_URI, mapper.getValues(linkFilter));
        if(uri != null) {
            Log.d("FilterService", "Inserted " + uri);
        }
        else {
            Log.e("FilterService", "Error inserting filter");
        }
    }

    private void updateLinkFilter(Uri uri, LinkFilter linkFilter) {
        int updated = getContentResolver().update(uri, mapper.getValues(linkFilter), null, null);
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
}
