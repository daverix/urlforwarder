package net.daverix.urlforward

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*

class AddFilterViewModel : ViewModel() {
    private val _name = MutableStateFlow("my filter")
    val name: StateFlow<String> = _name

    private val _urlInput = MutableStateFlow(
        FilterInput(
            pattern = ".*",
            patternIr = parseRegex(".*"),
            groups = listOf(MatchGroup(name = "@url", urlEncode = true)),
            visibleGroupCount = 1
        )
    )
    val urlFilter: StateFlow<FilterInput> = _urlInput

    private val _subjectInput = MutableStateFlow(
        FilterInput(
            pattern = ".*",
            patternIr = parseRegex(".*"),
            groups = listOf(MatchGroup(name = "@subject", urlEncode = true)),
            visibleGroupCount = 1
        )
    )
    val subjectFilter: StateFlow<FilterInput> = _subjectInput

    private val _outputUrl = MutableStateFlow("http://example.com?url=@url&subject=@subject")
    val outputUrl: StateFlow<String> = _outputUrl

    private val _testUrl = MutableStateFlow("")
    val testUrl: StateFlow<String> = _testUrl

    private val _testSubject = MutableStateFlow("")
    val testSubject: StateFlow<String> = _testSubject

    val testOutput: StateFlow<CreateUrlResult> = combine(
        urlFilter,
        subjectFilter,
        testUrl,
        testSubject,
        outputUrl
    ) { urlInput, subjectInput, testUrl, testSubject, outputUrl ->
        tryCreateUrl(
            urlPattern = urlInput.pattern,
            urlGroups = urlInput.groups,
            subjectPattern = subjectInput.pattern,
            subjectGroups = subjectInput.groups,
            inputUrl = testUrl,
            inputSubject = testSubject,
            outputUrl = outputUrl
        )
    }.stateIn(viewModelScope, started = SharingStarted.Eagerly, CreateUrlResult.NoMatch)

    fun updateName(name: String) {
        _name.value = name
    }

    fun updateUrlPattern(text: String) {
        _urlInput.updatePattern(text)
    }

    fun updateUrlGroupName(index: Int, name: String) {
        _urlInput.updateGroupName(index, name)
    }

    fun updateUrlGroupUrlEncode(index: Int, urlEncode: Boolean) {
        _urlInput.updateGroupUrlEncode(index, urlEncode)
    }

    fun updateSubjectPattern(text: String) {
        _subjectInput.updatePattern(text)
    }


    fun updateSubjectGroupName(index: Int, name: String) {
        _subjectInput.updateGroupName(index, name)
    }

    fun updateSubjectGroupUrlEncode(index: Int, urlEncode: Boolean) {
        _subjectInput.updateGroupUrlEncode(index, urlEncode)
    }

    private fun MutableStateFlow<FilterInput>.updateGroupName(
        index: Int,
        name: String
    ) {
        value = value.updateGroup(index) { group ->
            group.copy(name = name)
        }
    }

    private fun MutableStateFlow<FilterInput>.updateGroupUrlEncode(
        index: Int,
        urlEncode: Boolean
    ) {
        value = value.updateGroup(index) { group ->
            group.copy(urlEncode = urlEncode)
        }
    }

    private fun FilterInput.updateGroup(
        index: Int,
        transformGroup: (MatchGroup) -> MatchGroup
    ) = copy(
        groups = groups.toMutableList().also { groups ->
            groups[index] = transformGroup(groups[index])
        }
    )

    private fun MutableStateFlow<FilterInput>.updatePattern(text: String) {
        val ir = parseRegex(text)

        value = value.copy(
            pattern = text,
            patternIr = ir,
            visibleGroupCount = calculateGroups(ir) + 1 // +1 for group 0
        )
    }

    fun updateOutputUrl(text: String) {
        _outputUrl.value = text
    }

    private fun calculateGroups(element: RegexElement): Int = when (element) {
        is RegexIR -> element.children.sumBy { calculateGroups(it) }
        is Group -> 1 + element.children.sumBy { calculateGroups(it) }
        is Alternative -> calculateGroups(element.left) + calculateGroups(element.right)
        is ExactlyNumberOfOccurrences -> calculateGroups(element.child)
        is MaximumNumberOfOccurrences -> calculateGroups(element.child)
        is MinimumNumberOfOccurrences -> calculateGroups(element.child)
        is OneOrMoreOccurrences -> calculateGroups(element.child)
        is RangedNumberOfOccurrences -> calculateGroups(element.child)
        is ZeroOrMoreOccurrences -> calculateGroups(element.child)
        is ZeroOrOneOccurrences -> calculateGroups(element.child)
        is Token -> 0
        Wildcard -> 0
    }

    fun updateTestUrl(text: String) {
        _testUrl.value = text
    }

    fun updateTestSubject(text: String) {
        _testSubject.value = text
    }

    fun saveFilter() {
        //TODO: add filter unless validation somewhere fails
    }

    fun cancel() {

    }
}
