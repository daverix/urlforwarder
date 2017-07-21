package net.daverix.urlforward

interface InsertFilterCallbacks : OnFilterInsertedListener,
        OnCancelledListener

interface UpdateFilterCallbacks: OnFilterUpdatedListener,
        OnCancelledListener,
        OnFilterDeletedListener

interface OnFilterInsertedListener {
    fun onFilterInserted()
}

interface OnFilterUpdatedListener {
    fun onFilterUpdated()
}

interface OnFilterDeletedListener {
    fun onFilterDeleted()
}

interface OnCancelledListener {
    fun onCancelled()
}