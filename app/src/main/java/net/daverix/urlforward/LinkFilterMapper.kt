package net.daverix.urlforward

import android.content.ContentValues
import android.database.Cursor

interface LinkFilterMapper {
    val columns: Array<String>
    fun mapFilter(cursor: Cursor): LinkFilter
    fun getValues(filter: LinkFilter): ContentValues
}
