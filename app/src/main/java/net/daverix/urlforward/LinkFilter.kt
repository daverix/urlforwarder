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

data class LinkFilter(
    val id: Long = 0,
    val name: String,
    val filterUrl: String,
    val replaceText: String,
    val replaceSubject: String,
    val created: Long,
    val updated: Long,
    val encoded: Boolean,
    val regexPattern: String
)

fun createInitialAddFilter(
    id: Long = 0L,
    created: Long = System.currentTimeMillis(),
    name: String = ""
) = LinkFilter(
    id = id,
    name = name,
    filterUrl = "http://example.com/?url=@url&subject=@subject",
    replaceText = "@url",
    replaceSubject = "@subject",
    created = created,
    updated = created,
    encoded = true,
    regexPattern = ""
)
