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

import android.net.Uri
import java.io.UnsupportedEncodingException
import java.net.URLEncoder

class UriFilterCombinerImpl : UriFilterCombiner {
    @Throws(UriCombinerException::class)
    override fun create(linkFilter: LinkFilter, url: String?, subject: String?): Uri {
        return try {
            val filterUrl = linkFilter.filterUrl
            val encodedUrl = if (linkFilter.encoded) URLEncoder.encode(url, "UTF-8") else url
            var filteredUrl = filterUrl
            val replaceText = linkFilter.replaceText
            if (replaceText != null && encodedUrl != null) {
                filteredUrl = filterUrl!!.replace(replaceText, encodedUrl)
            }
            val replaceSubject = linkFilter.replaceSubject
            if (replaceSubject != null && subject != null) {
                filteredUrl = filteredUrl!!.replace(replaceSubject, subject)
            }
            Uri.parse(filteredUrl)
        } catch (e: UnsupportedEncodingException) {
            throw UriCombinerException(e)
        }
    }
}