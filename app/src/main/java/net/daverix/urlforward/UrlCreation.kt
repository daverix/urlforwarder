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

import androidx.compose.runtime.Immutable
import java.net.URLEncoder
import java.util.regex.PatternSyntaxException

@Immutable
sealed class CreateUrlResult {
    data class Successful(val url: String) : CreateUrlResult()
    object NoMatch : CreateUrlResult()
    data class SyntaxError(val index: Int) : CreateUrlResult()
}

fun tryCreateUrl(
    urlPattern: String,
    subjectPattern: String,
    urlGroups: List<MatchGroup>,
    subjectGroups: List<MatchGroup>,
    inputUrl: String,
    inputSubject: String,
    outputUrl: String
): CreateUrlResult = try {
    val urlMatch = urlPattern.toRegex().matchEntire(inputUrl)
    val subjectMatch = subjectPattern.toRegex().matchEntire(inputSubject)

    if (urlMatch != null || subjectMatch != null) {
        var url = outputUrl
        urlGroups.forEachIndexed { index, group ->
            url = url.replace(
                oldValue = group.name,
                newValue = urlMatch?.getGroupValue(index, group.urlEncode) ?: ""
            )
        }

        subjectGroups.forEachIndexed { index, group ->
            url = url.replace(
                oldValue = group.name,
                newValue = subjectMatch?.getGroupValue(index, group.urlEncode) ?: ""
            )
        }

        CreateUrlResult.Successful(url)
    } else CreateUrlResult.NoMatch
} catch (ex: PatternSyntaxException) {
    CreateUrlResult.SyntaxError(index = ex.index)
}

private fun MatchResult.getGroupValue(index: Int, urlEncode: Boolean): String =
    groupValues[index].let { value ->
        if (urlEncode) URLEncoder.encode(value, "UTF-8") else value
    }
