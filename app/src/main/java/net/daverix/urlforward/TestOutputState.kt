package net.daverix.urlforward

sealed class TestOutputState {
    data class MatchingUrl(val url: String) : TestOutputState()

    object NoMatch : TestOutputState()
}
