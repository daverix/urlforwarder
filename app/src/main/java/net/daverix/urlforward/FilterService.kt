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

import android.app.IntentService
import android.content.Intent
import android.net.Uri
import android.util.Log
import net.daverix.urlforward.db.UrlForwarderContract

class FilterService : IntentService("FilterService") {
    private lateinit var mapper: LinkFilterMapper

    override fun onCreate() {
        super.onCreate()
        mapper = LinkFilterMapperImpl()
    }

    override fun onHandleIntent(intent: Intent?) {
        val filter: LinkFilter? = intent?.getParcelableExtra(EXTRA_LINK_FILTER)
        if(filter == null) {
            Log.e("FilterService", "Filter not found")
            return
        }
        when(intent.action) {
            Intent.ACTION_INSERT -> insertLinkFilter(filter)
            Intent.ACTION_EDIT -> updateLinkFilter(intent.data, filter)
            Intent.ACTION_DELETE -> deleteLinkFilter(intent.data)
        }
    }

    private fun insertLinkFilter(linkFilter: LinkFilter) {
        val uri = contentResolver.insert(UrlForwarderContract.UrlFilters.CONTENT_URI, mapper.getValues(linkFilter))
        if (uri != null) {
            Log.d("FilterService", "Inserted $uri")
        } else {
            Log.e("FilterService", "Error inserting filter")
        }
    }

    private fun updateLinkFilter(uri: Uri?, linkFilter: LinkFilter) {
        val updated = contentResolver.update(uri!!, mapper.getValues(linkFilter), null, null)
        if (updated > 0) {
            Log.d("FilterService", "Updated $uri")
        } else {
            Log.e("FilterService", "Error updating filter")
        }
    }

    private fun deleteLinkFilter(uri: Uri?) {
        val deleted = contentResolver.delete(uri!!, null, null)
        if (deleted > 0) {
            Log.d("FilterService", "Deleted $uri")
        } else {
            Log.e("FilterService", "Error deleting filter $uri")
        }
    }

    companion object {
        const val EXTRA_LINK_FILTER = "linkFilter"
    }
}