package net.daverix.urlforward.db

import android.arch.persistence.room.TypeConverter
import java.util.*

object DbTypeConverters {
    @JvmStatic @TypeConverter
    fun toInt(value: Boolean): Int = if (value) 1 else 0

    @JvmStatic @TypeConverter
    fun toBoolean(value: Int): Boolean = value == 1

    @JvmStatic @TypeConverter
    fun toLong(value: Date): Long = value.time

    @JvmStatic @TypeConverter
    fun toDate(value: Long): Date = Date(value)
}