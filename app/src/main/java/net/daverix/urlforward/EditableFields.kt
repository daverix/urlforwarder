package net.daverix.urlforward

interface EditableFields {
    fun updateName(name: String)
    fun updateFilterUrl(url: String)
    fun updateRegex(regexPattern: String)
    fun updateReplaceUrl(url: String)
    fun updateReplaceSubject(subject: String)
    fun updateEncoded(encoded: Boolean)
}
