package net.daverix.urlforward.db


import android.database.sqlite.SQLiteDatabase

import javax.inject.Provider

class DatabaseProvider(private var dbFactory: UrlForwarderDatabaseHelper) : Provider<SQLiteDatabase> {
    override fun get() : SQLiteDatabase {
        return dbFactory.writableDatabase
    }
}
