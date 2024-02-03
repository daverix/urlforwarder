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

object UrlForwarderContract {
    object UrlFilterColumns {
        const val TITLE = "title"
        const val FILTER = "url"
        const val REPLACE_TEXT = "replace_text"
        const val CREATED = "created"
        const val UPDATED = "updated"
        const val SKIP_ENCODE = "skipEncode"
        const val REPLACE_SUBJECT = "replace_subject"
        const val REGEX_PATTERN = "regex_pattern"
    }
}
