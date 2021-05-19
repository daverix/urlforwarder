package net.daverix.urlforward

sealed class ParseRegexResult {
    data class ParseError(val text: String, val column: Int) : ParseRegexResult()

    data class Successful(val rootNode: RegexElement) : ParseRegexResult()
}
