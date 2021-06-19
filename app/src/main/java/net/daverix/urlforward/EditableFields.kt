package net.daverix.urlforward

interface EditableFields {
    fun updateTitle(title: String)
    fun updateFilterUrl(url: String)
    fun updateReplaceUrl(url: String)
    fun updateReplaceSubject(subject: String)
    fun updateEncoded(encoded: Boolean)
}