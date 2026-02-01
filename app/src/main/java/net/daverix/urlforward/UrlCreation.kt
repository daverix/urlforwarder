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

import java.net.URLEncoder.encode

fun createUrl(filter: LinkFilter, text: String, subject: String?): String? {
    val textMatches = replaceableParts(
        key = filter.replaceText,
        pattern = filter.textPattern.toRegex(),
        text = text,
        encodePart = filter.encoded
    ).takeIf { it.isNotEmpty() } ?: return null

    val subjectMatches = subject?.let {
        replaceableParts(
            key = filter.replaceSubject,
            pattern = filter.subjectPattern.toRegex(),
            text = it,
            encodePart = filter.encoded
        )
    } ?: emptyMap()

    val partMatches = textMatches + subjectMatches

    val textVariable = Regex.escape(filter.replaceText)
    val subjectVariable = Regex.escape(filter.replaceSubject)

    val variableNameRegex = "($textVariable|$subjectVariable)([0-9]+)?".toRegex()

    val outputUrl = filter.filterUrl.replace(variableNameRegex) { match ->
        partMatches[match.value] ?: ""
    }
    return outputUrl
}

private fun replaceableParts(
    key: String,
    pattern: Regex,
    text: String,
    encodePart: Boolean
): Map<String, String> = (key.takeIf { it.isNotEmpty() }
    ?.let { nonEmptyKey ->
        pattern.matchEntire(text)?.let { match ->
            match.groupValues.mapIndexed { index, value ->
                "$nonEmptyKey$index" to encodeText(encodePart, value)
            }.toMap() + (nonEmptyKey to encodeText(encodePart, match.value))
        }
    }
    ?: emptyMap())

private fun encodeText(encodePart: Boolean, value: String): String =
    if (encodePart) encode(value, "UTF-8") else value

