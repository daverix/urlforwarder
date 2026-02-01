package net.daverix.urlforward

interface EditableFields {
    fun updateName(name: String)
    fun updateFilterUrl(url: String)
    fun updateReplaceUrl(url: String)
    fun updateReplaceSubject(subject: String)
    fun updateEncoded(encoded: Boolean)
    fun updateTextPattern(pattern: String)
    fun updateSubjectPattern(pattern: String)
}
