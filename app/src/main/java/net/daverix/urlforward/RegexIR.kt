package net.daverix.urlforward

sealed class RegexElement

data class RegexIR(val children: List<RegexElement>) : RegexElement()

data class Alternative(val left: RegexElement, val right: RegexElement): RegexElement()

data class Group(val children: List<RegexElement>) : RegexElement()

data class Token(val text: String) : RegexElement()

object Wildcard : RegexElement()

data class ZeroOrOneOccurrences(val child: RegexElement) : RegexElement()

data class ZeroOrMoreOccurrences(val child: RegexElement) : RegexElement()

data class OneOrMoreOccurrences(val child: RegexElement) : RegexElement()

data class ExactlyNumberOfOccurrences(val child: RegexElement, val count: Int) : RegexElement()

data class MinimumNumberOfOccurrences(val child: RegexElement, val count: Int) : RegexElement()

data class MaximumNumberOfOccurrences(val child: RegexElement, val count: Int) : RegexElement()

data class RangedNumberOfOccurrences(val child: RegexElement, val min: Int, val max: Int) : RegexElement()

fun parseRegex(text: String): RegexIR {

    return RegexIR(children = listOf(Token(text)))
}
