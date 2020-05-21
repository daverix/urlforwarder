package net.daverix.urlforward

import android.net.Uri


interface Event

object Deleted : Event

object Saved : Event

object Cancel : Event

class EditFilter(val filterId: Long) : Event

class OpenLink(val uri: Uri) : Event

object CreateFilter : Event
