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

import java.net.URLEncoder

fun createUrl(linkFilter: LinkFilter, url: String?, subject: String?): String {
    var filteredUrl = linkFilter.filterUrl


    val replaceText = linkFilter.replaceText
    if (replaceText.isNotEmpty() && url != null) {
        val encodedUrl = if (linkFilter.encoded) URLEncoder.encode(url, "UTF-8") else url
        filteredUrl = filteredUrl.replace(replaceText, encodedUrl)
    }

    val replaceSubject = linkFilter.replaceSubject
    if (replaceSubject.isNotEmpty() && subject != null) {
        filteredUrl = filteredUrl.replace(replaceSubject, URLEncoder.encode(subject, "UTF-8"))
    }

    if (url != null && url.matches(Regex(linkFilter.regexPattern))) {
        val group_values = Regex(linkFilter.regexPattern).matchEntire(url)!!.groupValues
        val group_values_without_full_match = group_values.subList(1, group_values.size)
        // List<Pair<\1,first_group_value>>
        val endless_regex_group_strings: List<String> = generateSequence(1) { it + 1}
            .map { "\\${it}"}
            .take(group_values_without_full_match.size).toList()

        val regex_group_with_group_values: List<Pair<String,String>> = group_values_without_full_match.zip(endless_regex_group_strings)

        if (regex_group_with_group_values != null  && regex_group_with_group_values.isNotEmpty() && url != null) {
            for ((replacement, to_replace) in regex_group_with_group_values) {
                filteredUrl = filteredUrl.replace(to_replace, replacement)
            }
        }
    }

    return filteredUrl
}
