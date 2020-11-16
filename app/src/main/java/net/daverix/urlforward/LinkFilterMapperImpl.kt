package net.daverix.urlforward

import android.content.ContentValues
import android.database.Cursor
import android.provider.BaseColumns
import net.daverix.urlforward.db.UrlForwarderContract.UrlFilterColumns

class LinkFilterMapperImpl : LinkFilterMapper {
    override val columns: Array<String>
        get() = arrayOf(
                BaseColumns._ID,
                UrlFilterColumns.TITLE,
                UrlFilterColumns.FILTER,
                UrlFilterColumns.REPLACE_TEXT,
                UrlFilterColumns.CREATED,
                UrlFilterColumns.UPDATED,
                UrlFilterColumns.SKIP_ENCODE,
                UrlFilterColumns.REPLACE_SUBJECT
        )

    override fun mapFilter(cursor: Cursor): LinkFilter = LinkFilter().apply {
        id = cursor.getLong(0)
        title = cursor.getString(1)
        filterUrl = cursor.getString(2)
        replaceText = cursor.getString(3)
        created = cursor.getLong(4)
        updated = cursor.getLong(5)
        encoded = cursor.getShort(6).toInt() != 1
        replaceSubject = cursor.getString(7)
    }

    override fun getValues(filter: LinkFilter): ContentValues = ContentValues().apply {
        put(UrlFilterColumns.CREATED, filter.created)
        put(UrlFilterColumns.UPDATED, filter.updated)
        put(UrlFilterColumns.TITLE, filter.title)
        put(UrlFilterColumns.FILTER, filter.filterUrl)
        put(UrlFilterColumns.REPLACE_TEXT, filter.replaceText)
        put(UrlFilterColumns.SKIP_ENCODE, !filter.encoded)
        put(UrlFilterColumns.REPLACE_SUBJECT, filter.replaceSubject)
    }
}