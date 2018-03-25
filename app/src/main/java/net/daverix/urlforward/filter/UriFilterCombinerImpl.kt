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
package net.daverix.urlforward.filter

import android.net.Uri
import androidx.net.toUri
import io.reactivex.Single
import net.daverix.urlforward.dao.LinkFilterDao
import net.daverix.urlforward.dao.getFilter
import java.net.URLEncoder
import javax.inject.Inject

class UriFilterCombinerImpl @Inject constructor(private val filterDao: LinkFilterDao) : UriFilterCombiner {
    override fun create(filterId: Long, url: String, subject: String): Single<Uri> {
        return filterDao.getFilter(filterId).map { filter ->
            val replacement = if (!filter.skipEncode) URLEncoder.encode(url, "UTF-8") else url
            var filteredUrl = filter.filterUrl.replace(filter.replaceText, replacement)

            filteredUrl = filteredUrl.replace(filter.replaceSubject, subject)

            filteredUrl.toUri()
        }
    }
}
