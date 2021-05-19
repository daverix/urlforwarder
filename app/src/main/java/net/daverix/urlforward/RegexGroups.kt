package net.daverix.urlforward

sealed class CountRegexGroupsResult {
    data class Count(val count: Int) : CountRegexGroupsResult()
    data class InvalidStart(val index: Int) : CountRegexGroupsResult()
    data class InvalidEnd(val index: Int) : CountRegexGroupsResult()
}

fun String.tryCountRegexGroups(): CountRegexGroupsResult {
    var started = 0
    var ended = 0
    var i=0
    var lastStart = -1
    while (i < length) {
        // skip escaped characters
        if(this[i] == '\\') {
            i += 2
        }

        if(i >= length)
            break

        when(this[i]) {
            '(' -> {
                lastStart = i
                started++
            }
            ')' -> ended++
        }

        if(ended > started)
            return CountRegexGroupsResult.InvalidEnd(index = i)

        i++
    }

    if(started > ended)
        return CountRegexGroupsResult.InvalidStart(index = lastStart)

    return CountRegexGroupsResult.Count(started)
}
