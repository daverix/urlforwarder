package net.daverix.urlforward.dao

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.provider.BaseColumns
import java.util.*

const val FILTER_TITLE = "title"
const val FILTER_URL = "url"
const val FILTER_REPLACE_TEXT = "replace_text"
const val FILTER_CREATED = "created"
const val FILTER_UPDATED = "updated"
const val FILTER_SKIP_ENCODE = "skipEncode"
const val FILTER_REPLACE_SUBJECT = "replace_subject"
const val TABLE_FILTER = "filter"

@Entity(tableName =  TABLE_FILTER)
data class LinkFilter(@PrimaryKey(autoGenerate = true)
                      @ColumnInfo(name = BaseColumns._ID)
                      var id: Long,

                      @ColumnInfo(name = FILTER_TITLE)
                      var title: String = "",

                      @ColumnInfo(name = FILTER_URL)
                      var filterUrl: String,

                      @ColumnInfo(name = FILTER_REPLACE_TEXT)
                      var replaceText: String,

                      @ColumnInfo(name = FILTER_REPLACE_SUBJECT)
                      var replaceSubject: String,

                      @ColumnInfo(name = FILTER_CREATED)
                      var created: Date,

                      @ColumnInfo(name = FILTER_UPDATED)
                      var updated: Date,

                      @ColumnInfo(name = FILTER_SKIP_ENCODE)
                      var skipEncode: Boolean)
